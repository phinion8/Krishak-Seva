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
import com.phinion.planthelix.databinding.QuestionPhotoItemBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.LanguageItem

class ChoosedPhotoAdapter(private val context: Context, private val photoList: List<String>, private val removeImageCallback: RemoveImageCallback): RecyclerView.Adapter<ChoosedPhotoAdapter.ChoosedPhotoViewHolder>() {

    class ChoosedPhotoViewHolder(private val context: Context,private val binding: QuestionPhotoItemBinding, private val removeImageCallback: RemoveImageCallback): RecyclerView.ViewHolder(binding.root){

        fun bind(data: String, position: Int){

            binding.removeBtn.setOnClickListener {

                removeImageCallback.removeImageFromList(position)

            }
            Glide.with(context)
                .load(data)
                .into(binding.choosedPhoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoosedPhotoAdapter.ChoosedPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QuestionPhotoItemBinding.inflate(inflater, parent, false)
        return ChoosedPhotoAdapter.ChoosedPhotoViewHolder(context, binding, removeImageCallback)
    }

    override fun onBindViewHolder(holder: ChoosedPhotoAdapter.ChoosedPhotoViewHolder, position: Int) {

        val choosedPhoto = photoList[position]
        holder.bind(choosedPhoto, position)


    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}