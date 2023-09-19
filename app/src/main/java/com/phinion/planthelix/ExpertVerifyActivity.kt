package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phinion.planthelix.databinding.ActivityExpertMainBinding
import com.phinion.planthelix.databinding.ActivityExpertVerifyBinding

class ExpertVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertVerifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}