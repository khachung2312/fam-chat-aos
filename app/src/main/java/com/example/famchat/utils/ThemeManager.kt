package com.example.famchat.utils


import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {
    private const val THEME_KEY = "theme_mode"

    fun applyTheme() {
        val themeMode = PreferencesUtils.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun setTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        PreferencesUtils.putInt(THEME_KEY, mode)
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun isDarkThemeEnabled(): Boolean {
        return PreferencesUtils.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) == AppCompatDelegate.MODE_NIGHT_YES
    }
}
