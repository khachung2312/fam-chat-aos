package com.example.famchat.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.famchat.config.Constants

object PreferencesUtils {

    private lateinit var sharedPreferences: SharedPreferences
    private val KEY_DATA_EKC_ORDER = "KEY_DATA_EKC_ORDER"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("MySign", Context.MODE_PRIVATE);
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getBoolean(key: String, defaultvalue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultvalue)
    }

    fun getString(key: String?, defaultString: String? = null): String {
        return sharedPreferences.getString(key, defaultString) ?: ""
    }

    fun getInt(key: String?, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }


    fun clearLoginFinger() {
        putString(Constants.Finger.FINGER_PRINT, "")
        putString(Constants.Finger.FINGER_PRINT_KEY, "")
        putString(Constants.Finger.FINGER_PRINT_KEY_IV, "")
    }

}