package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phinion.planthelix.databinding.ActivityWalletBinding
import com.phinion.planthelix.models.FarmerUser

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var availableCoins : Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            database.collection("users")
                .document(userId)
                .addSnapshotListener{value, error->

                    val userItem = value?.toObject(FarmerUser::class.java)

                    availableCoins = userItem?.coins?.toLong()!!

                }
        }


    }
}