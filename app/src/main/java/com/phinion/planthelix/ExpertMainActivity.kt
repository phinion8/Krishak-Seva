package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.CropQuestionAdapter
import com.phinion.planthelix.adapters.ExpertQuestionCallback
import com.phinion.planthelix.databinding.ActivityExpertMainBinding
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.models.ExpertUser
import com.phinion.planthelix.utils.LoadingDialog

class ExpertMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val fragment = supportFragmentManager.findFragmentById(R.id.shopFragment)
        fragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}