package com.example.famchat.activity.splash

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.famchat.activity.BaseActivity
import com.example.famchat.config.Constants
import com.example.famchat.databinding.FcActivitySplashBinding
import com.example.famchat.extensions.fadeOut
import com.example.famchat.extensions.setStatusBarHomeTransparentHideNavi
import com.example.famchat.extensions.setStatusBarIconColor
import com.example.famchat.navigation.openAuthScreen
import com.example.famchat.navigation.openMainScreen
import com.example.famchat.utils.PreferencesUtils
import com.example.famchat.utils.ThemeManager
import com.example.famchat.viewmodel.AuthViewModel
import com.example.famchat.viewmodel.GlobalValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class SplashActivity : BaseActivity<FcActivitySplashBinding, AuthViewModel>() {

    companion object {
        const val DATA_KEY_ACTION = "data key action"
        const val OPEN_SCREEN_NAME = "OPEN_SCREEN_NAME"

        fun start(
            context: Context,
            isCleanTop: Boolean = false,
            keyAction: String? = null,
            openScreenName: String = "",
        ) {
            val intent = Intent(context, SplashActivity::class.java)
            if (isCleanTop) {
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
            }
            keyAction?.let {
                intent.putExtra(DATA_KEY_ACTION, keyAction)
            }
            intent.putExtra(OPEN_SCREEN_NAME, openScreenName)
            context.startActivity(intent)
        }
    }

    private var openScreenName: String = ""
    private var secretKey: String = ""
    private var deepLinkScreenName: String = ""


    override fun initView() {
        globalViewModel.currentScreenName = ""
        if (GlobalValue.userData.hasValidateCloudCA) {
            cleanDataLogin()
        }
        setStatusBarHomeTransparentHideNavi()
        val isDark = ThemeManager.isDarkThemeEnabled()
        setStatusBarIconColor(!isDark)

        loadBgSplash()
    }

    private fun loadBgSplash() {
        val bgSplash = PreferencesUtils.getString(Constants.Preferences.BG_SPLASH)

        Glide.with(this)
            .load(bgSplash)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivLogo.fadeOut()
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.bgSplash)
    }

    override fun initData() {
        lifecycleScope.launch {
            delay(500)
            handlerLogin()
        }
    }

    private fun handlerLogin() {
        lifecycleScope.launch {
            val userLogin = withContext(Dispatchers.IO) {
                viewModel.getUserLastLogin()
            }

            if (userLogin != null) {
                globalViewModel.userLogin = userLogin.convertUserLoginModel()
                openMainScreen()
            } else {
                openAuthScreen()
            }

            finish()
        }
    }


    override fun initViewModel() {
        super.initViewModel()
    }

    override fun initListener() {
        /** initListener */
    }

    override fun getViewBinding(): FcActivitySplashBinding {
        return FcActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }


}