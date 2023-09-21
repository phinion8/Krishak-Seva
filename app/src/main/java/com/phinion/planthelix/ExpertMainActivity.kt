package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.adapters.ExpertQuestionCallback
import com.phinion.planthelix.databinding.ActivityExpertMainBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.ExpertUser
import com.phinion.planthelix.utils.LoadingDialog

class ExpertMainActivity : AppCompatActivity(), ExpertQuestionCallback {
    private lateinit var binding: ActivityExpertMainBinding
    private lateinit var questionAdapter: CropQuestionAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var cropQuestionList: ArrayList<CropIssueQuestion> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.firestore


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        questionAdapter = CropQuestionAdapter(this, cropQuestionList, this)

        with(binding) {
            questionList.apply {
                layoutManager = LinearLayoutManager(this@ExpertMainActivity)
                adapter = questionAdapter
            }
        }

        firebaseAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener{value, error->

                    val userItem = value?.toObject(ExpertUser::class.java)
                    if (userItem != null) {
                        binding.expertName.text = "Hey! Expert ${userItem.name}"
                    }

                }
        }

        firebaseAuth.currentUser?.let {
            database.collection("questions")
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
                        if (cropQuestionList.size <= 0){
                            binding.noQuestionText.visibility = View.VISIBLE
                        }
                    }
                }
        }




    }

    override fun expertQuestionOnClick(position: Int) {
        val questionItem = cropQuestionList[position]
        Intent(this, ExpertQuestionDetailActivity::class.java).apply {
            putExtra("userId", questionItem.userId)
            putExtra("userDocumentId", questionItem.userDocumentId)
            startActivity(this)
        }
    }
}