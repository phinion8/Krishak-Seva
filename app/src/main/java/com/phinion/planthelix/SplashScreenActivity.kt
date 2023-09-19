package com.phinion.planthelix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phinion.planthelix.databinding.ActivitySplashScreenBinding
import com.phinion.planthelix.models.ExpertUser
import com.phinion.planthelix.models.FarmerUser
import com.phinion.planthelix.screens.LanguageSelectionActivity
import com.phinion.planthelix.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    var isExpert = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        firebaseAuth = FirebaseAuth.getInstance()

        database = FirebaseFirestore.getInstance()


        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)

            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let {
                    database.collection("users").document(it.uid)
                        .addSnapshotListener { value, error ->

                            val expertUser = value?.get("expertUser") as Boolean
                            this@SplashScreenActivity.showToast(expertUser.toString())
                            if (expertUser != null) {
                                isExpert = expertUser
                            }

                            if (isExpert) {
                                Intent(this@SplashScreenActivity, ExpertMainActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }

                            } else {
                                Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }
                            }

                        }
                }


            } else {
                Intent(this@SplashScreenActivity, LanguageSelectionActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
    }
}