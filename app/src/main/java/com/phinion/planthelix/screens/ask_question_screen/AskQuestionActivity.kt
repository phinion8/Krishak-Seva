package com.phinion.planthelix.screens.ask_question_screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Config
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.phinion.planthelix.MainActivity
import com.phinion.planthelix.R
import com.phinion.planthelix.adapters.ChoosedPhotoAdapter
import com.phinion.planthelix.adapters.RemoveImageCallback
import com.phinion.planthelix.databinding.ActivityAskQuestionBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.utils.Constants
import com.phinion.planthelix.utils.LoadingDialog
import com.phinion.planthelix.utils.PhotoChooserDialog
import com.phinion.planthelix.utils.Resource
import com.phinion.planthelix.utils.SuccessDialog
import com.phinion.planthelix.utils.Utils
import com.phinion.planthelix.utils.generateUniqueFileName
import com.phinion.planthelix.utils.isAndroidRAndAbove
import com.phinion.planthelix.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AskQuestionActivity : AppCompatActivity(), RemoveImageCallback {
    private lateinit var binding: ActivityAskQuestionBinding
    private val viewModel: AskQuestionViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var successDialog: SuccessDialog
    private lateinit var photoChooserDialog: PhotoChooserDialog
    private lateinit var photoAdapter: ChoosedPhotoAdapter
    private lateinit var storage: FirebaseStorage
    var availableCoins: Long = -1
    private val photoList = ArrayList<String>()
    lateinit var imageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        photoList.add(imageUri.toString())
        photoAdapter.notifyDataSetChanged()
        if (photoAdapter.itemCount > 0) {
            binding.noImgText.visibility = View.GONE
        }
    }
    val requestCode = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        imageUri = createImageUri()!!
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        loadingDialog = LoadingDialog(context = this)
        successDialog = SuccessDialog(context = this)
        photoChooserDialog = PhotoChooserDialog(context = this, cameraOnClick = {

            contract.launch(imageUri)
            photoChooserDialog.dismissPhotoPickerDialog()

        }, galleryOnClick = {
            invokeAttachFileIntent(Constants.ATTACHMENT_CHOICE_CHOOSE_GALLERY)
            photoChooserDialog.dismissPhotoPickerDialog()
        })

        val userId = auth.currentUser?.uid

        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val cameraPermission = Manifest.permission.CAMERA
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            if (ContextCompat.checkSelfPermission(this, cameraPermission) != granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission, cameraPermission),
                    requestCode
                )
            }
            // Permission is not granted; request it
        } else {
            // Permission is already granted; you can access the gallery
            // Perform your gallery-related operations here
        }

        photoAdapter = ChoosedPhotoAdapter(this, photoList, this)

        binding.backBtn.setOnClickListener {
            finish()
        }

        if (userId != null) {
            database.collection("users")
                .document(userId)
                .addSnapshotListener { value, error ->

                    val userCoins = value?.getLong("coins")
                    if (userCoins != null) {
                        availableCoins = userCoins
                    }

                }
        }

        binding.addImageBtn.setOnClickListener {
            photoChooserDialog.showPhotoPickerDialog()
        }


        binding.photoList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoList.adapter = photoAdapter







        binding.sendBtn.setOnClickListener {
            if (availableCoins >= 200) {


                loadingDialog.showLoadingDialog()
                val question = binding.etQuestion.text.toString()
                val downloadUrlList: ArrayList<String> = ArrayList()
                val questionDes = binding.etQuestionDes.text.toString()
                val storageRef = storage.reference.child("userPhotos")

                if (question.isNotEmpty() && questionDes.isNotEmpty()) {
                    if (userId != null) {
                        val totalImages = photoList.size
                        var uploadedImages = 0 // Counter for uploaded images

                        for (images in photoList) {
                            val fileName = generateUniqueFileName("png")
                            val imageRef = storageRef.child("$userId/$fileName")
                            val uploadTask = imageRef.putFile(Uri.parse(images))

                            uploadTask.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Get the download URL for the uploaded image
                                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                                        downloadUrlList.add(uri.toString())
                                        uploadedImages++

                                        // Check if all images have been uploaded
                                        if (uploadedImages == totalImages) {
                                            // All images are uploaded, now add the question to the database
                                            CoroutineScope(Dispatchers.Main).launch {
                                                viewModel.addQuestionToDatabase(
                                                    CropIssueQuestion(
                                                        "",
                                                        userId,
                                                        "",
                                                        downloadUrlList,
                                                        question,
                                                        questionDes,
                                                        ""
                                                    )
                                                ).collect {
                                                    when (it) {
                                                        is Resource.Loading -> {
                                                            // Already showing the loading dialog
                                                        }

                                                        is Resource.Success -> {

                                                            database.collection("users")
                                                                .document(userId)
                                                                .update(
                                                                    "coins",
                                                                    FieldValue.increment(-200)
                                                                )
                                                                .addOnSuccessListener {
                                                                    loadingDialog.dismissLoadingDialog()
                                                                    successDialog.showSuccessDialog(
                                                                        getString(R.string.success),
                                                                        getString(R.string.question_asked_successfully_expect_a_response_from_an_expert_within_24_hours),
                                                                        okOnClick = {
                                                                            successDialog.dismissSuccessDialog()
                                                                            val intent = Intent(
                                                                                this@AskQuestionActivity,
                                                                                MainActivity::class.java
                                                                            )
                                                                            startActivity(intent)
                                                                            finish()
                                                                        }
                                                                    )

                                                                }


                                                        }

                                                        is Resource.Error -> {
                                                            this@AskQuestionActivity.showToast(it.message)
                                                            loadingDialog.dismissLoadingDialog()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Handle the error
                                    this@AskQuestionActivity.showToast(
                                        task.exception?.message ?: getString(R.string.upload_failed)
                                    )
                                    loadingDialog.dismissLoadingDialog()
                                }
                            }
                        }
                    }
                } else {
                    this.showToast(getString(R.string.question_or_description_cannot_be_empty))
                }
            }else{
                this.showToast(getString(R.string.insufficient_coins))
            }

        }


    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.phinion.planthelix.fileProvider",
            image
        )

    }

    private fun invokeAttachFileIntent(attachmentChoice: Int) {
        when (attachmentChoice) {
            Constants.ATTACHMENT_CHOICE_CHOOSE_GALLERY -> {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(galleryIntent)
                return
            }

        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val selectedImageUri: Uri? = data?.data
            photoList.add(selectedImageUri.toString())
            photoAdapter.notifyDataSetChanged()
            if (photoAdapter.itemCount > 0) {
                binding.noImgText.visibility = View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; you can access the gallery
                // Perform your gallery-related operations here
            } else {
                // Permission denied; handle the case where the user didn't grant permission
            }
        }
    }

    override fun removeImageFromList(position: Int) {
        photoList.removeAt(position)
        photoAdapter.notifyDataSetChanged()
        if (photoAdapter.itemCount <= 0) {
            binding.noImgText.visibility = View.VISIBLE
        }
    }


}