package com.example.famchat.navigation

import android.net.Uri
import com.example.famchat.activity.BaseActivity
import com.example.famchat.activity.login.LoginActivity
import com.example.famchat.activity.main.MainActivity
import com.example.famchat.activity.splash.AuthActivity
import com.example.famchat.activity.splash.SplashActivity
import com.example.famchat.viewmodel.GlobalValue
import com.example.mimiAlpha.activity.registerAccount.RegisterAccountActivity
import com.example.mimiAlpha.activity.webview.WebViewActivity

fun isSignedIn(
    onSuccess: () -> Unit = {},
    onFail: () -> Unit = {},
) {
    if (GlobalValue.userData.accessTokenMySign.isNotEmpty()) onSuccess.invoke()
    else onFail.invoke()
}

/**
 * Open screen splash
 *
 * @param keyAction: key from deeplink/notification
 * @param isCleanTop: clear all screen current
 * */
fun BaseActivity<*, *>.openSplashScreen(
    isCleanTop: Boolean = false,
    keyAction: String? = null,
    openScreenName: String = "",
) {
    SplashActivity.start(this, isCleanTop, keyAction, openScreenName)
}


/**
 * Open recharge screen
 * */
fun BaseActivity<*, *>.openWebView(
    title: String = "",
    urlWeb: String,
    urlSuccess: String? = null,
    successListener: (() -> Unit)? = null
) {
    WebViewActivity.start(this, title, urlWeb, urlSuccess, successListener)
}


/**
 * Open screen login
 *
 * @param isCleanTop: clear all screen current
 * */
fun BaseActivity<*, *>.openLoginScreen(
    isCleanTop: Boolean = false,
    data: Uri? = null,
    openNextScreenName: String = "",
) {
    LoginActivity.start(this, isCleanTop, data, openNextScreenName)
}

fun BaseActivity<*, *>.openAuthScreen(
) {
    AuthActivity.start(this)
}



/**
 * Open screen register activity
 * */
fun BaseActivity<*, *>.openRegisterAccountActivity() {
    RegisterAccountActivity.start(this)
}


/**
 * Open screen home
 * */
fun BaseActivity<*, *>.openMainScreen() {
    MainActivity.start(this, intent?.data)
}

fun BaseActivity<*, *>.openMainScreenFromData(uri: Uri) {
    MainActivity.start(this, uri)
}


fun BaseActivity<*, *>.openScreenOrLogout(userName: String, openScreen: () -> Unit = {}) {
    isSignedIn(
        onSuccess = {
            if (globalViewModel.userInformation.value?.userId == userName) {
                openScreen.invoke()
            } else {
                GlobalValue.userData.accessTokenMySign = ""
                openLoginScreen(true)
            }
        },
        onFail = {
            openLoginScreen(true)
        }
    )
}

fun BaseActivity<*, *>.openScreenWithLogin(data: Uri? = null, openScreen: () -> Unit = {}) {
    isSignedIn(
        onSuccess = {
            openScreen.invoke()
        },
        onFail = {
            openLoginScreen(true, data = data)
        }
    )
}