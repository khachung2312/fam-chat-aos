package com.example.famchat.utils

import android.util.Log
import com.example.famchat.BuildConfig

object LogUtil {

    private const val TAG = "LogMySign"
    private const val MAX_LOG = 2000

    fun logE(data: String?, tag: String? = TAG) {
        if (BuildConfig.DEBUG) {
            data?.let {
                var index = 0
                val length = data.length
                while (index < length) {
                    val endIndex = if (index + MAX_LOG > length) length else index + MAX_LOG
                    val chunk = data.substring(index, endIndex)
                    Log.e(tag, chunk)
                    index += MAX_LOG
                }
            } ?: run {
                Log.e(tag, "")
            }
        }
    }

    fun logI(data: String?, tag: String? = TAG) {
        if (BuildConfig.DEBUG) {
            data?.let {
                var index = 0
                val length = data.length
                while (index < length) {
                    val endIndex = if (index + MAX_LOG > length) length else index + MAX_LOG
                    val chunk = data.substring(index, endIndex)
                    Log.i(tag, chunk)
                    index += MAX_LOG
                }
            } ?: run {
                Log.i(tag, "")
            }
        }
    }

    fun logD(data: String?, tag: String? = TAG) {
        if (BuildConfig.DEBUG) {
            data?.let {
                var index = 0
                val length = data.length
                while (index < length) {
                    val endIndex = if (index + MAX_LOG > length) length else index + MAX_LOG
                    val chunk = data.substring(index, endIndex)
                    Log.d(tag, chunk)
                    index += MAX_LOG
                }
            } ?: run {
                Log.d(tag, "")
            }
        }
    }

}