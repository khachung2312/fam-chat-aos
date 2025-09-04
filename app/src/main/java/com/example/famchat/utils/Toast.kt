package com.example.famchat.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

object Toast {

    const val TIME_SHOW = 2500
    fun showToast(context: Context?, stringResourceId: Int) {
        if (context == null) return
        Toast.makeText(context, context.getText(stringResourceId), Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context?, message: String?) {
        if (context == null || TextUtils.isEmpty(message)) return
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToastShort(context: Context?, message: String?) {
        if (context == null || TextUtils.isEmpty(message)) return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastShort(context: Context?, stringResourceId: Int) {
        if (context == null) return
        Toast.makeText(context, context.getText(stringResourceId), Toast.LENGTH_SHORT).show()
    }

}