package com.phinion.planthelix.screens

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinion.planthelix.R
import com.phinion.planthelix.adapters.LanguageAdapter
import com.phinion.planthelix.adapters.LanguageSelectionCallback
import com.phinion.planthelix.databinding.ActivityLanguageSelectionBinding
import com.phinion.planthelix.databinding.LanguageItemLayoutBinding
import com.phinion.planthelix.models.LanguageItem
import com.phinion.planthelix.utils.setLocale
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity(), LanguageSelectionCallback {
    private lateinit var languageSelectionBinding: ActivityLanguageSelectionBinding
    private lateinit var languageAdapter: LanguageAdapter
    private var languageList: List<LanguageItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageSelectionBinding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(languageSelectionBinding.root)

        window.statusBarColor = resources.getColor(R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        languageList = listOf(
            LanguageItem("1", "English", getString(R.string.heal_your_plant_in_your_language)),
            LanguageItem("2", "हिंदी", "अपने पौधे को अपनी भाषा में ठीक करें।")
        )

        languageAdapter = LanguageAdapter(this, languageList, this)

        languageSelectionBinding.rvLanguage.apply {
            layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            adapter = languageAdapter
            setHasFixedSize(true)
        }

        languageSelectionBinding.englishBtn.setOnClickListener {
            setLocale(this, "en")
            languageSelectionBinding.englishBtn.setBackgroundResource(R.drawable.language_selected_background)
            languageSelectionBinding.tvLangTitle.setTextColor(ContextCompat.getColor(this, R.color.blue))
            languageSelectionBinding.tvLangDes.setTextColor(ContextCompat.getColor(this, R.color.blue))
            languageSelectionBinding.tvGreet.setText("Namaste!")
            languageSelectionBinding.selectAppLangText.setText("Select your app language.")
            languageSelectionBinding.termsText.setText("By selecting you agree to our privacy policy and terms and conditions.")
            languageSelectionBinding.continueBtn.setText("Continue")
            languageSelectionBinding.hindiBtn.setBackgroundResource(R.drawable.language_selector_background)
            languageSelectionBinding.hindiTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
            languageSelectionBinding.hindiLangDes.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        languageSelectionBinding.hindiBtn.setOnClickListener {
            setLocale(this, "hi")
            languageSelectionBinding.hindiBtn.setBackgroundResource(R.drawable.language_selected_background)
            languageSelectionBinding.hindiTitle.setTextColor(ContextCompat.getColor(this, R.color.blue))
            languageSelectionBinding.hindiLangDes.setTextColor(ContextCompat.getColor(this, R.color.blue))
            languageSelectionBinding.tvGreet.setText("नमस्ते!")
            languageSelectionBinding.selectAppLangText.setText("ऐप भाषा चुनें|")
            languageSelectionBinding.termsText.setText("चयन करके आप हमारी गोपनीयता नीति और नियम व शर्तों से सहमत होते हैं।")
            languageSelectionBinding.continueBtn.setText("जरी राखे|")
            languageSelectionBinding.englishBtn.setBackgroundResource(R.drawable.language_selector_background)
            languageSelectionBinding.tvLangTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
            languageSelectionBinding.tvLangDes.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        languageSelectionBinding.continueBtn.setOnClickListener{
            val intent = Intent(this, SignOptionSelectionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun languageItemOnClick(position: Int, binding: LanguageItemLayoutBinding) {

        val languageItem = languageList[position]



        when(languageItem.id){
            "1" -> {
                setLocale(this, "en")
                binding.languageSelectionLayout.setBackgroundResource(R.drawable.language_selected_background)
                binding.tvLangTitle.setTextColor(ContextCompat.getColor(this, R.color.blue))
                binding.tvLangDes.setTextColor(ContextCompat.getColor(this, R.color.blue))
                binding.langRadio.isChecked = true
            }

            "2" -> {
                setLocale(this,"hi")
                languageSelectionBinding.tvGreet.setText("नमस्ते!")
                languageSelectionBinding.selectAppLangText.setText("ऐप भाषा चुनें|")
                languageSelectionBinding.termsText.setText("चयन करके आप हमारी गोपनीयता नीति और नियम व शर्तों से सहमत होते हैं।")
                languageSelectionBinding.continueBtn.setText("जरी राखे|")
                binding.languageSelectionLayout.setBackgroundResource(R.drawable.language_selected_background)
                binding.tvLangTitle.setTextColor(ContextCompat.getColor(this, R.color.blue))
                binding.tvLangDes.setTextColor(ContextCompat.getColor(this, R.color.blue))
                binding.langRadio.isChecked = true
            }
        }

    }

}