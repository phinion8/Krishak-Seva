package com.phinion.planthelix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.adapters.ExpertQuestionCallback
import com.phinion.planthelix.databinding.ActivityRecentQuestionsBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.screens.ask_question_screen.AskQuestionActivity

class RecentQuestionsActivity : AppCompatActivity(), ExpertQuestionCallback {
    private lateinit var binding: ActivityRecentQuestionsBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var cropQuestionList: ArrayList<CropIssueQuestion> = ArrayList()
    private lateinit var questionAdapter: CropQuestionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        questionAdapter = CropQuestionAdapter(this, cropQuestionList, this)

        with(binding) {
            backBtn.setOnClickListener {
                finish()
            }
            rvQuestionList.apply {
                layoutManager = LinearLayoutManager(this@RecentQuestionsActivity)
                adapter = questionAdapter
            }


        }

        auth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .collection("askedQuestions")
                .addSnapshotListener{value, error->

                    if (value != null) {
                        cropQuestionList.clear()
                        for (snapshot in value.documents){
                            val cropQuestionItem = snapshot.toObject(CropIssueQuestion::class.java)
                            if (cropQuestionItem != null) {
                                cropQuestionList.add(cropQuestionItem)
                            }
                        }
                        questionAdapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                        if (cropQuestionList.size <= 0){
                            binding.noQuestionText.visibility = View.VISIBLE
                        }
                    }
                }
        }


    }

    override fun expertQuestionOnClick(position: Int) {
        val questionItem = cropQuestionList[position]
        Intent(this, UserQuestionDetail::class.java).apply {
            putExtra("userId", questionItem.userId)
            putExtra("userDocumentId", questionItem.userDocumentId)
            startActivity(this)
        }
    }
}