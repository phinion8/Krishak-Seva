package com.phinion.planthelix.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.phinion.planthelix.R

class ExpertSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_sign_up)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR



    }
}