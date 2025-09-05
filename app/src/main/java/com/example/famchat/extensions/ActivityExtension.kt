package com.example.famchat.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity


inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

fun Activity.hideSoftKeyboard() {
    window?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}

fun Context.findActivity(): BaseActivity<*, *>? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is BaseActivity<*, *>) return ctx
        ctx = ctx.baseContext
    }
    return null
}


fun Activity.hideSoftKeyboard(view: View) {
    window?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showSoftKeyboard() {
    window?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun Activity.toast(content: String?) {
    runOnUiThread {
        if (!TextUtils.isEmpty(content)) Toast.makeText(
            this,
            content.addDotLastString(),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Activity.toast(stringResourceId: Int) {
    runOnUiThread {
        Toast.makeText(
            this,
            this.getString(stringResourceId).addDotLastString(),
            Toast.LENGTH_SHORT
        ).show()
    }
}

@SuppressLint("RestrictedApi")
fun Activity.isFingerprintExists(): Boolean {
    try {
        val fingerprintManager = FingerprintManagerCompat.from(this)
        return fingerprintManager.isHardwareDetected
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return false
}

@SuppressLint("RestrictedApi")
fun Activity.isFingerPrintRegistered(): Boolean {
    val fingerprintManager = FingerprintManagerCompat.from(this)
    return fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
}

fun Activity.openStore() {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
    )
}

fun Activity.openBrowser(url: String) {
    if (TextUtils.isEmpty(url)) {
        return
    }
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Activity.setStatusBarHomeTransparent() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //make fully Android Transparent Status bar
    val winParams = window.attributes
    winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
    window.attributes = winParams
    window.statusBarColor = Color.TRANSPARENT
}


fun Activity.setStatusBarIconColor(isDark: Boolean) {
    val decorView = window.decorView

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsetsController = WindowInsetsControllerCompat(window, decorView)
        windowInsetsController.isAppearanceLightStatusBars = isDark
    } else {
        var flags = decorView.systemUiVisibility
        flags = if (isDark) {
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = flags
    }
}


fun Activity.setStatusBarHomeTransparentHideNavi() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //make fully Android Transparent Status bar
    val winParams = window.attributes
    winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
    window.attributes = winParams
    window.statusBarColor = Color.TRANSPARENT
}

fun Activity.setStatusBarHomePrimary() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_VISIBLE
    val winParams = window.attributes
    window.attributes = winParams
    window.statusBarColor = resources.getColor(R.color.fc_color_primary_dark)
}

fun Activity.getHeightStatusBar(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Activity.getWidthScreen(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Activity.getHeightScreen(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}