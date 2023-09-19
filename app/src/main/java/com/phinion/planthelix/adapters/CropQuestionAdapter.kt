package com.phinion.planthelix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.CropQuestionItemLayoutBinding
import com.phinion.planthelix.databinding.LanguageItemLayoutBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.LanguageItem

class CropQuestionAdapter(private val context: Context, private val questionList: List<CropIssueQuestion>): RecyclerView.Adapter<CropQuestionAdapter.CropQuestionViewHolder>() {

    class CropQuestionViewHolder(private val context: Context,private val binding: CropQuestionItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(cropIssueQuestion: CropIssueQuestion){

            binding.tvQuestion.text = cropIssueQuestion.question
            binding.tvDesctiption.text = cropIssueQuestion.questionDescription

            if (cropIssueQuestion.image.isNotEmpty()){
                Glide.with(context)
                    .load(cropIssueQuestion.image[0])
                    .into(binding.questionImg)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropQuestionAdapter.CropQuestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CropQuestionItemLayoutBinding.inflate(inflater, parent, false)
        return CropQuestionViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: CropQuestionAdapter.CropQuestionViewHolder, position: Int) {

        val questionItem = questionList[position]
        holder.bind(questionItem)


    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}