package com.example.famchat.config

import com.example.famchat.R

enum class LanguageSetting(var languageCode: String, var flag: Int, var nameDisplay: String) {
    VIETNAMESE("vi", R.drawable.ca_ic_flag_vn, "Tiếng Việt"),
    ENGLISH("en", R.drawable.ca_ic_flag_en, "English")
}