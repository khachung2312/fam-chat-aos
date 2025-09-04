package com.example.famchat.listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastRequestSign : BroadcastReceiver() {

    companion object {
        const val ACTION_NOTIFY_REQUEST = "action notify request"
    }

    var requestSignListener: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_NOTIFY_REQUEST) {
                requestSignListener?.invoke()
            }
        }
    }

}