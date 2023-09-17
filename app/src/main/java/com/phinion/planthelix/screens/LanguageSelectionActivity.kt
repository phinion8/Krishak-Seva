package com.phinion.planthelix.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinion.planthelix.R
import com.phinion.planthelix.adapters.LanguageAdapter
import com.phinion.planthelix.databinding.ActivityLanguageSelectionBinding
import com.phinion.planthelix.models.LanguageItem

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageSelectionBinding
    private lateinit var languageAdapter: LanguageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val languageList = listOf(
            LanguageItem("1", "English", "Heal your plant in your language."),
            LanguageItem("2", "हिंदी", "अपने पौधे को अपनी भाषा में ठीक करें।")
        )

        languageAdapter = LanguageAdapter(this, languageList)

        binding.rvLanguage.apply {
            layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            adapter = languageAdapter
            setHasFixedSize(true)
        }

        binding.continueBtn.setOnClickListener{
            val intent = Intent(this, SignOptionSelectionActivity::class.java)
            startActivity(intent)
        }
    }
}