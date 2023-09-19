package com.phinion.planthelix.screens.fragments.home_screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.databinding.FragmentHomeBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.screens.ask_question_screen.AskQuestionActivity
import com.phinion.planthelix.utils.Resource
import com.phinion.planthelix.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var questionAdapter: CropQuestionAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var cropQuestionList: ArrayList<CropIssueQuestion> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.firestore





        questionAdapter = CropQuestionAdapter(requireContext(), cropQuestionList)

        with(binding) {
            rvQuestionList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = questionAdapter
            }

            askQuestionBtn.setOnClickListener {
                Intent(requireContext(), AskQuestionActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        firebaseAuth.currentUser?.let {
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
                    }
                }
        }

        return binding.root
    }

}