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
import com.phinion.planthelix.ExpertQuestionDetailActivity
import com.phinion.planthelix.R
import com.phinion.planthelix.RecentQuestionsActivity
import com.phinion.planthelix.UserQuestionDetail
import com.phinion.planthelix.WalletActivity
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.adapters.ExpertQuestionCallback
import com.phinion.planthelix.databinding.FragmentHomeBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.ExpertUser
import com.phinion.planthelix.screens.ask_question_screen.AskQuestionActivity
import com.phinion.planthelix.utils.LoadingDialog
import com.phinion.planthelix.utils.Resource
import com.phinion.planthelix.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ExpertQuestionCallback {
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

        binding.coinAnim.setOnClickListener {
            Intent(requireContext(), WalletActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.seeAllBtn.setOnClickListener {
            Intent(requireContext(), RecentQuestionsActivity::class.java).apply {
                startActivity(this)
            }
        }

        questionAdapter = CropQuestionAdapter(requireContext(), cropQuestionList, this)

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
                .addSnapshotListener{value, error->

                    val userItem = value?.toObject(ExpertUser::class.java)
                    if (userItem != null) {
                        binding.tvUserName.text = getString(R.string.hey) + userItem.name
                        binding.tvAvailableCoins.text = "${userItem.coins}"
                    }

                }
        }

        firebaseAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .collection("askedQuestions")
                .limit(1)
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
                            binding.etNoQuestion.visibility = View.VISIBLE
                        }
                    }
                }
        }

        return binding.root
    }

    override fun expertQuestionOnClick(position: Int) {

        val questionItem = cropQuestionList[position]
        Intent(requireContext(), UserQuestionDetail::class.java).apply {
            putExtra("userId", questionItem.userId)
            putExtra("userDocumentId", questionItem.userDocumentId)
            startActivity(this)
        }

    }

}