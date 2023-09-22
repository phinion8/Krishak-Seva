package com.phinion.planthelix.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phinion.planthelix.AdsItem
import com.phinion.planthelix.ProductDetailActivity
import com.phinion.planthelix.databinding.AdsItemLayoutBinding
import com.phinion.planthelix.databinding.ProductItemLayoutBinding
import com.phinion.planthelix.models.ProductItem

class ProductAdapter(private val context: Context, private val productList: ArrayList<ProductItem>, private val productItemCallback: ProductItemCallback): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ProductItemLayoutBinding, private val productItemCallback: ProductItemCallback): RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context, productItem: ProductItem, position: Int){
            Glide.with(context)
                .load(productItem.productImage)
                .into(binding.productImg)
            binding.productTitle.text = productItem.title
            binding.productDes.text = productItem.productDescription
            binding.productSize.text = productItem.size[0]
            binding.productPrice.text = "From ₹${productItem.price}"
            binding.root.setOnClickListener {
                Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("categoryId", productItem.category)
                    putExtra("productId", productItem.id)
                    context.startActivity(this)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemLayoutBinding.inflate(inflater, parent, false)
        return ProductAdapter.ProductViewHolder(binding, productItemCallback)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {

        val productItem = productList[position]
        holder.bind(context, productItem, position)


    }

    override fun getItemCount(): Int {
        return productList.size
    }
}