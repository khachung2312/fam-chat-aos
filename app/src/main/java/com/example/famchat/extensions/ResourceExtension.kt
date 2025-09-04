package com.example.famchat.extensions

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

import android.util.DisplayMetrics

import java.util.Locale

fun Context.resStringLanguage(id: Int, lang: String): String {
    //Get default locale to back it
    val res: Resources = resources
    val conf: Configuration = res.configuration
    val savedLocale: Locale = conf.locale
    //Retrieve resources from desired locale
    val confAr: Configuration = resources.configuration
    confAr.locale = Locale(lang)
    val metrics = DisplayMetrics()
    val resources = Resources(assets, metrics, confAr)
    //Get string which you want
    val string = resources.getString(id)
    //Restore default locale
    conf.locale = savedLocale
    res.updateConfiguration(conf, null)
    //return the string that you want
    return string
}