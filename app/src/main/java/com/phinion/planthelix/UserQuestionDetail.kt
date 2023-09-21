package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ColorStateListInflaterCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.databinding.ActivityUserQuestionDetailBinding
import com.phinion.planthelix.models.CropIssueQuestion

class UserQuestionDetail : AppCompatActivity() {
    private lateinit var binding: ActivityUserQuestionDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

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
                    .addSnapshotListener{value, error->

                        val questionItem = value?.toObject(CropIssueQuestion::class.java)
                        if (questionItem != null) {
                            binding.questionText.text = questionItem.question
                            binding.questionDesText.text = questionItem.questionDescription

                            Glide.with(this)
                                .load(questionItem.image[0])
                                .into(binding.imageView10)
                        }

                        if (questionItem?.expertAnswer?.isNotEmpty() == true || questionItem?.expertAnswer?.isNotBlank() == true){
                            binding.stausText.text = getString(R.string.status_answered)
                            binding.answerText.text = questionItem.expertAnswer
                            binding.stausText.setTextColor(ContextCompat.getColor(this, R.color.green))
                        }else{
                            binding.answerText.text = getString(R.string.sorry_no_answer_yet_our_experts_are_working_hard_to_answer_the_question_please_be_paitent)
                            binding.stausText.text = getString(R.string.status_unanswered)
                            binding.stausText.setTextColor(ContextCompat.getColor(this, R.color.red))
                        }

                    }
            }
        }


    }
}