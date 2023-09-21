package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.databinding.ActivityEarnCoinsBinding

class EarnCoinsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEarnCoinsBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarnCoinsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore


    }
}