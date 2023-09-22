package com.phinion.planthelix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.phinion.planthelix.databinding.ActivityMainBinding
import com.phinion.planthelix.screens.chat_bot_screen.ChatBotActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.chatBotBtn.setOnClickListener{
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

        navController = Navigation.findNavController(this, R.id.navHostFragment)
        setupWithNavController(binding.bottomNav, navController)

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