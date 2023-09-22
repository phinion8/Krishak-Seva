package com.phinion.planthelix.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phinion.planthelix.AdsItem
import com.phinion.planthelix.models.LanguageItem
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.AdsItemLayoutBinding
import com.phinion.planthelix.databinding.LanguageItemLayoutBinding
import com.phinion.planthelix.screens.LanguageSelectionActivity

class AdsAdapter(private val context: Context, private val adsList: ArrayList<AdsItem>, private val earnCoinCallback: EarnCoinCallback): RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    class AdsViewHolder(private val binding: AdsItemLayoutBinding, private val earnCoinCallback: EarnCoinCallback): RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context, adsItem: AdsItem, position: Int){
            Glide.with(context)
                .load(adsItem.image)
                .into(binding.adsImg)
            binding.adsTitle.text = adsItem.title
            binding.adsDes.text = adsItem.description
            binding.detailsBtn.setOnClickListener {
                earnCoinCallback.adsOnClick(position)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsAdapter.AdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdsItemLayoutBinding.inflate(inflater, parent, false)
        return AdsAdapter.AdsViewHolder(binding, earnCoinCallback)
    }

    override fun onBindViewHolder(holder: AdsAdapter.AdsViewHolder, position: Int) {

        val adsItem = adsList[position]
        holder.bind(context, adsItem, position)


    }

    override fun getItemCount(): Int {
        return adsList.size
    }
}