package com.example.mimiAlpha.activity.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.famchat.activity.BaseActivity
import com.example.famchat.databinding.FcActivityWebviewBinding
import com.example.famchat.viewmodel.base.BaseViewModel

class WebViewActivity : BaseActivity<FcActivityWebviewBinding, BaseViewModel>() {

    companion object {

        private const val DATA_URL_WEB = "data url web"
        private const val DATA_URL_SUCCESS = "data url success"
        private const val DATA_TITLE = "data title"

        fun start(
            activity: BaseActivity<*, *>,
            title: String = "",
            urlWeb: String,
            urlSuccess: String? = null,
            successListener: (() -> Unit)? = null
        ) {
            val intent = Intent(activity, WebViewActivity::class.java).apply {
                putExtra(DATA_URL_WEB, urlWeb)
                urlSuccess?.let {
                    putExtra(DATA_URL_SUCCESS, urlSuccess)
                }
                putExtra(DATA_TITLE, title)
            }
            activity.launchActivityResult.launch(intent) {
                if (it.resultCode == RESULT_OK) {
                    successListener?.invoke()
                }
            }
        }
    }

    private val urlWeb: String by lazy {
        intent?.getStringExtra(DATA_URL_WEB) ?: ""
    }

    private val title: String by lazy {
        intent?.getStringExtra(DATA_TITLE) ?: ""
    }

    private val urlSuccess: String? by lazy {
        intent?.getStringExtra(DATA_URL_SUCCESS)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        binding.webView.settings.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
            databaseEnabled = true
            builtInZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, titleWeb: String) {
                //onReceivedTitle
            }


        }
    }

    override fun initData() {
        binding.webView.apply {
            loadUrl(urlWeb)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    checkActionSuccess(request?.url.toString())
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        checkActionSuccess(url)
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

            }
        }
    }

    private fun checkActionSuccess(urlProgress: String?) {
        if (TextUtils.isEmpty(urlProgress) || TextUtils.isEmpty(urlSuccess)) {
            return
        }
        if (urlProgress!!.contains(urlSuccess.toString())) {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun initListener() {
        //initListener
    }

    override fun onDestroy() {
        try {
            binding.webView.destroy()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        super.onDestroy()
    }

    override fun getViewBinding(): FcActivityWebviewBinding {
        return FcActivityWebviewBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }
}