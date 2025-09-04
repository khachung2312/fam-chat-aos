package com.example.famchat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import com.example.famchat.activity.BaseActivity
import com.example.famchat.extensions.safeParseInt
import com.example.famchat.utils.PreferencesUtils
import com.example.famchat.viewmodel.AuthViewModel
import com.example.famchat.viewmodel.GlobalValue
import com.example.famchat.viewmodel.LoginViewModel


class FamChatManager : Application.ActivityLifecycleCallbacks {

    var currentActivity: BaseActivity<*, *>? = null
    private var timerLogout: CountDownTimer? = null
    private var numStarted = -1
    private lateinit var viewModel: AuthViewModel

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: FamChatManager? = null

        fun instance(): FamChatManager {
            if (instance == null)
                instance = FamChatManager()
            return instance!!
        }
    }

    fun init(application: Application) {
        val globalValue = GlobalValue
        globalValue.clearUserData()
        PreferencesUtils.init(application)
        application.registerActivityLifecycleCallbacks(this)
        viewModel = AuthViewModel(application)
    }

    fun startTimerLogout() {
        if (TextUtils.isEmpty(GlobalValue.userData.accessTokenMySign)) {
            return
        }
        timerLogout?.let {
            it.cancel()
        }
        val timeout =
            GlobalValue.appConfigData?.timeOutSession?.timeout?.safeParseInt(10)!! * 60 * 1000L
        timerLogout = object : CountDownTimer(timeout, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //onTick
            }

            override fun onFinish() {
                /**
                 * Bỏ qua những màn hình không yêu cầu login
                 * */
                if (!TextUtils.isEmpty(GlobalValue.userData.accessTokenMySign)) {
                    currentActivity?.logout()
                }
            }
        }.start()
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        if (activity is BaseActivity<*, *>) {
            currentActivity = activity
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0) {
//            viewModel.getAllConfig {  }
        } else if (numStarted == -1) numStarted = 0
        numStarted += 1
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is BaseActivity<*, *>) {
            currentActivity = activity
        }
    }

    override fun onActivityPaused(activity: Activity) {
        // onActivityPaused
    }

    override fun onActivityStopped(activity: Activity) {
        numStarted -= 1
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        // onActivitySaveInstanceState
    }

    override fun onActivityDestroyed(activity: Activity) {
        // onActivityDestroyed
    }

}