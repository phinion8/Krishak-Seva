package com.phinion.planthelix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.phinion.planthelix.databinding.ActivitySplashScreenBinding
import com.phinion.planthelix.screens.LanguageSelectionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        firebaseAuth = FirebaseAuth.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            if (firebaseAuth.currentUser != null){
                Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }else{
                Intent(this@SplashScreenActivity, LanguageSelectionActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
    }
}