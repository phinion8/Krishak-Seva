package com.phinion.planthelix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.phinion.planthelix.models.LanguageItem
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.LanguageItemLayoutBinding

class LanguageAdapter(private val context: Context, private val languageList: List<LanguageItem>): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    class LanguageViewHolder(private val binding: LanguageItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context, languageItem: LanguageItem){
            binding.tvLangTitle.text = languageItem.languageName
            binding.tvLangDes.text = languageItem.des
            binding.languageSelectionLayout.setOnClickListener {

                binding.languageSelectionLayout.setBackgroundResource(R.drawable.language_selected_background)
                binding.tvLangTitle.setTextColor(ContextCompat.getColor(context, R.color.blue))
                binding.tvLangDes.setTextColor(ContextCompat.getColor(context, R.color.blue))
                binding.langRadio.isChecked = true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LanguageItemLayoutBinding.inflate(inflater, parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {

        val languageItem = languageList[position]
        holder.bind(context, languageItem)


    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}