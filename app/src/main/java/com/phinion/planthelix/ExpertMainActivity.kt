package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phinion.planthelix.databinding.ActivityExpertMainBinding

class ExpertMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}