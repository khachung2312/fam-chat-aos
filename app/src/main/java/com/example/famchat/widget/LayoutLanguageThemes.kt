package com.example.famchat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.famchat.config.Constants
import com.example.famchat.config.LanguageSetting
import com.example.famchat.databinding.FcItemLanguageOptionBinding
import com.example.famchat.databinding.FcLayoutLanguageThemesBinding
import com.example.famchat.databinding.FcLayoutSelectLanguageBinding
import com.example.famchat.extensions.findActivity
import com.example.famchat.extensions.setSafeOnClickListener
import com.example.famchat.extensions.showCustomPopupWindowBinding
import com.example.famchat.utils.PreferencesUtils
import com.example.famchat.R
import com.example.famchat.config.Constants.THEMES.THEME_DARK
import com.example.famchat.config.Constants.THEMES.THEME_LIGHT
import com.example.famchat.config.Constants.THEMES.THEME_SYSTEM
import com.example.famchat.databinding.FcLayoutSelectThemesBinding
import com.example.famchat.extensions.loadImage
import com.example.famchat.extensions.setStatusBarIconColor
import com.example.famchat.utils.ThemeManager

class LayoutLanguageThemes @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: FcLayoutLanguageThemesBinding

    init {
        initView()
    }

    private fun initView() {
        binding = FcLayoutLanguageThemesBinding.inflate(LayoutInflater.from(context), this, true)

        if (isInEditMode) return

        updateLanguageIcon()

        binding.ivLanguage.setSafeOnClickListener {
            val currentLangCode = PreferencesUtils.getString(Constants.Preferences.KEY_LANGUAGE, "vi")

            context.findActivity()?.let { activity ->
                binding.ivLanguage.showCustomPopupWindowBinding<FcLayoutSelectLanguageBinding>(activity) { popupBinding, popupWindow ->

                    val container = popupBinding.languageContainer
                    container.removeAllViews()

                    LanguageSetting.values().forEach { lang ->
                        val itemBinding = FcItemLanguageOptionBinding.inflate(
                            LayoutInflater.from(context), container, false
                        )

                        itemBinding.imgFlag.setImageResource(lang.flag)
                        itemBinding.tvLanguageName.text = lang.nameDisplay

                        if (lang.languageCode == currentLangCode) {
                            itemBinding.imgChecked.visibility = VISIBLE
                            itemBinding.root.setBackgroundResource(R.drawable.fc_bg_radius_e4e4e7_color)
                        } else {
                            itemBinding.imgChecked.visibility = GONE
                        }

                        itemBinding.root.setOnClickListener {
                            setAppLanguage(lang)
                            updateLanguageIcon()
                            popupWindow.dismiss()
                        }

                        container.addView(itemBinding.root)
                    }
                }
            }

        }



        updateThemeIcon()

        binding.ivThemes.setSafeOnClickListener {
            val currentTheme = PreferencesUtils.getString(Constants.Preferences.KEY_THEMES, "system")

            context.findActivity()?.let { activity ->

                binding.ivThemes.showCustomPopupWindowBinding<FcLayoutSelectThemesBinding>(activity) { popupBinding, popupWindow ->

                    when (currentTheme) {
                        "light" -> {
                            popupBinding.imgCheckLight.visibility = VISIBLE
                            popupBinding.layoutLight.setBackgroundResource(R.drawable.fc_bg_radius_e4e4e7_color)
                        }
                        "dark" -> {
                            popupBinding.imgCheckDark.visibility = VISIBLE
                            popupBinding.layoutDark.setBackgroundResource(R.drawable.fc_bg_radius_e4e4e7_color)
                        }
                        else -> {
                            popupBinding.imgCheckSystem.visibility = VISIBLE
                            popupBinding.layoutSystem.setBackgroundResource(R.drawable.fc_bg_radius_e4e4e7_color)
                        }
                    }

                    popupBinding.layoutLight.setOnClickListener {
                        setAppThemes("light")
                        updateThemeIcon()
                        popupWindow.dismiss()
                    }

                    popupBinding.layoutDark.setOnClickListener {
                        setAppThemes("dark")
                        updateThemeIcon()
                        popupWindow.dismiss()
                    }

                    popupBinding.layoutSystem.setOnClickListener {
                        setAppThemes("system")
                        updateThemeIcon()
                        popupWindow.dismiss()
                    }
                }
            }
        }

    }

    private fun updateLanguageIcon() {
        if (isInEditMode) return // Skip preview

        val currentLangCode = PreferencesUtils.getString(Constants.Preferences.KEY_LANGUAGE, "vi")
        val currentLang = LanguageSetting.values().firstOrNull { it.languageCode == currentLangCode }
        currentLang?.let {
            binding.ivLanguage.setImageResource(it.flag)
        }
    }


    private fun updateThemeIcon() {
        if (isInEditMode) return // Skip preview

        val currentTheme = PreferencesUtils.getString(Constants.Preferences.KEY_THEMES, "system")
        val resId = when (currentTheme) {
            "light" -> R.drawable.fc_ic_sun
            "dark" -> R.drawable.fc_ic_moon
            else -> R.drawable.fc_ic_tv_minimal
        }
        binding.ivThemes.setImageResource(resId)
    }


    private fun setAppLanguage(language: LanguageSetting) {
        PreferencesUtils.putString(Constants.Preferences.KEY_LANGUAGE, language.languageCode)
        context.findActivity()?.setLanguage(language.languageCode)
        binding.ivLanguage.loadImage(resource = language.flag)
    }

    private fun setAppThemes(themes: String) {
        PreferencesUtils.putString(Constants.Preferences.KEY_THEMES, themes)


        when (themes) {
            THEME_LIGHT -> {
                ThemeManager.setTheme(false)
                context.findActivity()?.setStatusBarIconColor(isDark = true)
            }
            THEME_DARK -> {
                ThemeManager.setTheme(true)
                context.findActivity()?.setStatusBarIconColor(isDark = false)
            }
            THEME_SYSTEM -> {
                PreferencesUtils.putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                val isDark = ThemeManager.isDarkThemeEnabled()
                context.findActivity()?.setStatusBarIconColor(!isDark)
            }
        }
    }
}
