package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.databinding.FragmentHomeBinding
import com.phinion.planthelix.models.CropIssueQuestion

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var questionAdapter: CropQuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val questionList = listOf(
            CropIssueQuestion(
                "1",
                "https://firebasestorage.googleapis.com/v0/b/crop-healix.appspot.com/o/plant_disease.jpg?alt=media&token=134cbd36-bb0a-4868-aaf7-ef89337bc4f7",
                "It is subjective evidence of disease or physical disturbance broadly or something that indicates the presence of bodily disorder",
                "A disease or disorder is a particular abnormal condition that negatively affects the structure or function of all or a part of an organism which occurs in plants, animals and humans. It is not due to any immediate external injury. Diseases are often known to be medical conditions that are associated with specific symptoms and signs which show during any disease."
            ),
            CropIssueQuestion(
                "1",
                "https://firebasestorage.googleapis.com/v0/b/crop-healix.appspot.com/o/plant_disease.jpg?alt=media&token=134cbd36-bb0a-4868-aaf7-ef89337bc4f7",
                "It is subjective evidence of disease or physical disturbance broadly or something that indicates the presence of bodily disorder",
                "A disease or disorder is a particular abnormal condition that negatively affects the structure or function of all or a part of an organism which occurs in plants, animals and humans. It is not due to any immediate external injury. Diseases are often known to be medical conditions that are associated with specific symptoms and signs which show during any disease."
            )
        )
        questionAdapter = CropQuestionAdapter(requireContext(), questionList)

        with(binding){
            rvQuestionList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = questionAdapter
                setHasFixedSize(true)
            }

            askQuestionBtn.setOnClickListener {
                Intent(requireContext(), AskQuestionActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        return binding.root
    }

}