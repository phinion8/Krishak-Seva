package com.phinion.planthelix

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.AdsAdapter
import com.phinion.planthelix.adapters.EarnCoinCallback
import com.phinion.planthelix.databinding.ActivityEarnCoinsBinding
import com.phinion.planthelix.utils.showToast

class EarnCoinsActivity : AppCompatActivity(), EarnCoinCallback {
    private lateinit var binding: ActivityEarnCoinsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: AdsAdapter
    private var adsList: ArrayList<AdsItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarnCoinsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore
        auth = Firebase.auth

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.backBtn.setOnClickListener {
            finish()
        }

        adapter = AdsAdapter(this, adsList, this)

        binding.rvAdsList.apply {
            layoutManager = LinearLayoutManager(this@EarnCoinsActivity)
            adapter = this@EarnCoinsActivity.adapter
        }

        database.collection("adsDetails")
            .addSnapshotListener{value, error->

                adsList.clear()
                for (snapshot in value?.documents!!){

                    val adsItem = snapshot.toObject(AdsItem::class.java)
                    if (adsItem != null) {
                        adsList.add(adsItem)
                    }

                }
                adapter.notifyDataSetChanged()


            }



    }

    private fun addCoinsToDatabase(coins: Long, link: String){
        auth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .update("coins", FieldValue.increment(coins))
                .addOnSuccessListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                    this.showToast("Loading...")
                    this.showToast("$coins Coins added successfully.")
                }

        }
    }

    override fun adsOnClick(position: Int) {
        val adsItem = adsList[position]
        addCoinsToDatabase(adsItem.coins.toLong(), adsItem.link)

    }
}