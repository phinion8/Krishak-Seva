package com.phinion.planthelix.adapters

import com.phinion.planthelix.databinding.LanguageItemLayoutBinding

interface LanguageSelectionCallback {
    fun languageItemOnClick(position: Int, binding: LanguageItemLayoutBinding)
}