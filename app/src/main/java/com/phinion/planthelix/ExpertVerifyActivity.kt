package com.phinion.planthelix

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.phinion.planthelix.databinding.ActivityExpertVerifyBinding
import com.phinion.planthelix.screens.ExpertLogInActivity
import com.phinion.planthelix.screens.ExpertSignUpActivity
import com.phinion.planthelix.utils.LoadingDialog
import com.phinion.planthelix.utils.SuccessDialog
import com.phinion.planthelix.utils.generateUniqueFileName
import com.phinion.planthelix.utils.showToast


class ExpertVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertVerifyBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var storage: FirebaseStorage
    private lateinit var successDialog: SuccessDialog
    private var fileUri: Uri? = null

    companion object {
        private const val PICK_PDF_REQUEST = 1
        private const val STORAGE_PERMISSION_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        storage = FirebaseStorage.getInstance()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        loadingDialog = LoadingDialog(this)
        successDialog = SuccessDialog(this)

        val permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        auth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener { value, error ->

                    val isVerified = value?.getBoolean("verified")
                    if (isVerified == true) {
                        startActivity(Intent(this, ExpertMainActivity::class.java))
                        finish()
                    }

                }
        }



        binding.fileSelectBtn.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(permissions)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report != null) {
                            if (report.areAllPermissionsGranted()) {
                                setupFilePicker()
                            }
                        } else {
                            this@ExpertVerifyActivity.showToast("Permission denied. You cannot select PDF files.")
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {


                    }


                }).check()
        }

        binding.expertVerifySendBtn.setOnClickListener {
            if (fileUri != null) {
                loadingDialog.showLoadingDialog()
                val storageRef = storage.reference.child("expertUserDocumentDetails")
                val fileName = generateUniqueFileName("pdf")
                val fileRef = storageRef.child("expertId/$fileName")
                val uploadTask = fileRef.putFile(fileUri!!)
                uploadTask.addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        fileRef.downloadUrl.addOnSuccessListener { uri ->
                            auth.currentUser?.let { it1 ->
                                database.collection("users")
                                    .document(it1.uid)
                                    .update("document", uri).addOnSuccessListener {
                                        loadingDialog.dismissLoadingDialog()
                                        successDialog.showSuccessDialog(
                                            getString(R.string.file_uploaded_successfully),
                                            getString(R.string.you_will_be_verified_within_24_hours),
                                            okOnClick = {
                                                val intent =
                                                    Intent(this, ExpertLogInActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            })
                                    }
                            }
                        }

                    } else {
                        this.showToast(getString(R.string.something_went_wrong))
                    }

                }
            }
        }


    }

    private fun setupFilePicker() {
        // Launch the PDF file picker when a button is clicked, for example.
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, PICK_PDF_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            // The user has successfully selected a PDF file.
            val pdfUri: Uri = data.data!!

            fileUri = pdfUri
            val fileName = getFileNameFromUri(pdfUri)
            binding.fileName.text = fileName

            // Now you can use the 'pdfUri' to upload the selected PDF to Firebase Storage or perform other actions.
        }


    }

    fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = cursor.getString(displayNameIndex)
                }
            }
        } finally {
            cursor?.close()
        }
        return fileName
    }


}