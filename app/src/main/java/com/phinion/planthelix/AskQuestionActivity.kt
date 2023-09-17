package com.phinion.planthelix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phinion.planthelix.databinding.ActivityAskQuestionBinding

class AskQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAskQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}