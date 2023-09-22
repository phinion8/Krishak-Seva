package com.phinion.planthelix

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.phinion.planthelix.databinding.ActivityProductDetailBinding
import com.phinion.planthelix.models.ProductItem

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var database: FirebaseFirestore
    private var phoneNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val productId = intent.getStringExtra("productId")
        val categoryId = intent.getStringExtra("categoryId")

        binding.contactShopBtn.setOnClickListener {
            openPhoneDialer(phoneNumber, this)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        if (productId != null) {
            if (categoryId != null) {
                database.collection("shopItems")
                    .document("CYyShemrupV4kPaX0PAF")
                    .collection(categoryId)
                    .document(productId)
                    .addSnapshotListener{value, error->

                        val productItem = value?.toObject(ProductItem::class.java)

                        if (productItem != null) {
                            binding.productTitle.text = productItem.title
                            binding.tvProductTitle.text = productItem.title
                            binding.tvProductCategory.text = "Category - ${productItem.category}"
                            binding.tvProductDes.text = productItem.productDescription
                            binding.tvDistance.text = productItem.distance
                            binding.tvShopTitle.text = productItem.shop
                            binding.tvProductPrice.text = "From ₹${productItem.price}"
                            binding.tvProductSize.text = "Size - ${productItem.size[0]}"
                            phoneNumber = productItem.shopPhoneNumber
                            Glide.with(this)
                                .load(productItem.productImage)
                                .into(binding.productImage)
                        }
                    }
            }
        }
    }
    fun openPhoneDialer(phoneNumber: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        val uri = Uri.parse("tel:$phoneNumber")
        intent.data = uri
        context.startActivity(intent)
    }
}