package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.adapters.ExpertQuestionCallback
import com.phinion.planthelix.databinding.FragmentExpertHomeBinding
import com.phinion.planthelix.databinding.FragmentHomeBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.ExpertUser


class ExpertHomeFragment : Fragment(), ExpertQuestionCallback {

    private lateinit var binding: FragmentExpertHomeBinding
    private lateinit var questionAdapter: CropQuestionAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var cropQuestionList: ArrayList<CropIssueQuestion> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExpertHomeBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.firestore


        questionAdapter = CropQuestionAdapter(requireContext(), cropQuestionList, this)

        with(binding) {
            questionList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = questionAdapter
            }
        }

        firebaseAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener{value, error->

                    val userItem = value?.toObject(ExpertUser::class.java)
                    if (userItem != null) {
                        binding.expertName.text = getString(R.string.hey_expert) + userItem.name
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



        return binding.root
    }

    override fun expertQuestionOnClick(position: Int) {
        val questionItem = cropQuestionList[position]
        Intent(requireContext(), ExpertQuestionDetailActivity::class.java).apply {
            putExtra("userId", questionItem.userId)
            putExtra("userDocumentId", questionItem.userDocumentId)
            startActivity(this)
        }
    }


}