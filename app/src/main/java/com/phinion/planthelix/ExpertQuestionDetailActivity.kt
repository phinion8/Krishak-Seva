package com.phinion.planthelix

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.databinding.ActivityExpertQuestionDetailBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.utils.LoadingDialog
import com.phinion.planthelix.utils.SuccessDialog
import com.phinion.planthelix.utils.showToast

class ExpertQuestionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertQuestionDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var successDialog: SuccessDialog
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        successDialog = SuccessDialog(this)
        loadingDialog = LoadingDialog(this)
        val questionUserId = intent.getStringExtra("userId")
        val userDocumentId = intent.getStringExtra("userDocumentId")

        binding.backBtn.setOnClickListener {
            finish()
        }


        if (questionUserId != null) {
            if (userDocumentId != null) {
                database.collection("users")
                    .document(questionUserId)
                    .collection("askedQuestions")
                    .document(userDocumentId)
                    .addSnapshotListener { value, error ->

                        val questionItem = value?.toObject(CropIssueQuestion::class.java)
                        if (questionItem != null) {
                            binding.questionText.text = questionItem.question
                            binding.questionDesText.text = questionItem.questionDescription

                            if (!isDestroyed) {
                                Glide.with(applicationContext)
                                    .load(questionItem.image[0])
                                    .into(binding.imageView10)
                            }

                        }

                    }
            }
        }

        binding.sendBtn.setOnClickListener {
            if (binding.etAnswer.text.toString().trim().isNotEmpty()) {
                loadingDialog.showLoadingDialog()
                val answer = binding.etAnswer.text.toString()
                if (questionUserId != null) {
                    if (userDocumentId != null) {
                        database.collection("users")
                            .document(questionUserId)
                            .collection("askedQuestions")
                            .document(userDocumentId)
                            .update("expertAnswer", answer)
                            .addOnSuccessListener {
                                loadingDialog.dismissLoadingDialog()
                                successDialog.showSuccessDialog(
                                    getString(R.string.success),
                                    getString(R.string.answer_sent_successfully),
                                    okOnClick = {
                                        successDialog.dismissSuccessDialog()
                                        val intent = Intent(this, ExpertMainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    })
                            }
                    }
                }
            } else {
                this.showToast(getString(R.string.answer_can_not_be_empty))
            }
        }


    }
}