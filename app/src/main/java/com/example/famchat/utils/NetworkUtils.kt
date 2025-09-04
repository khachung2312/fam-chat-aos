package com.example.famchat.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

object NetworkUtils {

    /**
     * Check network is connected
     * */
    fun isNetworkConnected(activity: AppCompatActivity): Boolean {
        return try {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        } catch (ex: Exception) {
            false
        }
    }

}