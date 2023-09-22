package com.phinion.planthelix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.phinion.planthelix.models.LanguageItem
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.LanguageItemLayoutBinding
import com.phinion.planthelix.screens.LanguageSelectionActivity

class LanguageAdapter(private val context: Context, private val languageList: List<LanguageItem>, private val languageSelectionCallback: LanguageSelectionCallback): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    class LanguageViewHolder(private val binding: LanguageItemLayoutBinding, private val languageSelectionCallback: LanguageSelectionCallback): RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context, languageItem: LanguageItem, position: Int){
            binding.tvLangTitle.text = languageItem.languageName
            binding.tvLangDes.text = languageItem.des
            binding.languageSelectionLayout.setOnClickListener {

                languageSelectionCallback.languageItemOnClick(position, binding = binding)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LanguageItemLayoutBinding.inflate(inflater, parent, false)
        return LanguageViewHolder(binding, languageSelectionCallback)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {

        val languageItem = languageList[position]
        holder.bind(context, languageItem, position)


    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}