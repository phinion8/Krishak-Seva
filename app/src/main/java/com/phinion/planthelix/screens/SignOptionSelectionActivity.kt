package com.phinion.planthelix.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.ActivitySignOptionSelectionBinding

class SignOptionSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignOptionSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignOptionSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.signInOptionRadioGroup.setOnCheckedChangeListener{group, checkedId->

            val radioButton = findViewById<RadioButton>(checkedId)


            when (radioButton.id) {
                R.id.farmer_radio -> {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }
                R.id.gardner_radio -> {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }

                R.id.expert_radio ->{
                    val intent = Intent(this, ExpertSignUpActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }
}