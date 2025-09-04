package com.example.famchat

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.example.famchat.config.Constants
import com.example.famchat.di.appModule
import com.example.famchat.utils.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        FamChatManager.instance().init(this)
        ThemeManager.applyTheme()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@BaseApp)
            modules(
                appModule
            )
        }
    }

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, Constants.DefaultValue.LANGUAGE_DEFAULT)
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(baseContext, super.getResources())
    }

}