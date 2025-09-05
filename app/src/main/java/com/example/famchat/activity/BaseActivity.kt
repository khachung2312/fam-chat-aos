package com.example.famchat.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.famchat.FamChatManager
import com.example.famchat.R
import com.example.famchat.activity.splash.SplashActivity
import com.example.famchat.dialog.progress.DialogProgress
import com.example.famchat.extensions.autoHideKeyboard
import com.example.famchat.extensions.hideSoftKeyboard
import com.example.famchat.listener.BroadcastRequestSign
import com.example.famchat.listener.BroadcastSms
import com.example.famchat.navigation.openSplashScreen
import com.example.famchat.utils.keyboard.KeyboardHeightProvider
import com.example.famchat.viewmodel.GlobalValue
import com.example.famchat.viewmodel.base.BaseViewModel
import com.example.famchat.viewmodel.globall.GlobalViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import org.koin.java.KoinJavaComponent.inject
import java.util.regex.Matcher
import java.util.regex.Pattern


@SuppressLint("WrongConstant")

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel> : PermissionActivity(),
    KeyboardHeightProvider.KeyboardListener {

    companion object {
        private const val SMS_CONSENT_REQUEST = 12345
    }

    lateinit var binding: B
    val globalViewModel: GlobalViewModel by inject<GlobalViewModel>(GlobalViewModel::class.java)

    val viewModel: VM by lazy {
        ViewModelProvider(this)[getViewModelClass()]
    }
    lateinit var dialogLoading: DialogProgress

    private val intentFilter by lazy {
        IntentFilter().apply {
            addAction(BroadcastRequestSign.ACTION_NOTIFY_REQUEST)
        }
    }
    val keyboardHeightProvider by lazy {
        KeyboardHeightProvider(this)
    }
    var keyboardChangeListener: ((Int) -> Unit)? = null
    private var broadcastNotify: BroadcastRequestSign? = null
    private val smsVerificationReceiver = BroadcastSms()
    private var timerLogout: CountDownTimer? = null
    private var otpListener: ((String) -> Unit)? = null

    protected abstract fun getViewBinding(): B

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    open var exceptionAutoHideKeyboard = mutableListOf<View>()

    open fun screenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    open fun getRootView(): View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = getRootView()
        if (rootView != null) {
            setContentView(rootView)
        } else {
            binding = getViewBinding()
            setContentView(binding.root)
        }

        setupBaseLogic()
    }

    private fun setupBaseLogic() {
        requestedOrientation = screenOrientation()
        binding.root.autoHideKeyboard(this, exceptionAutoHideKeyboard)
        dialogLoading = DialogProgress.ExtendBuilder(this)
            .setCancelable(false)
            .setCanOnTouchOutside(false)
            .build() as DialogProgress
        keyboardHeightProvider.addKeyboardListener(this)
        initView()
        initViewModel()
        initListener()
        initData()

        try {
            FamChatManager.instance().startTimerLogout()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        keyboardHeightProvider.onPause()
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
//        if (this !is SplashActivity && TextUtils.isEmpty(GlobalValue.sharedKey)) {
//            logout()
//        }
    }

    override fun onHeightChanged(height: Int) {
        keyboardChangeListener?.invoke(height)
    }

    protected open fun initViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            isLoading?.let {
                if (it) hideSoftKeyboard()
                showHideLoading(it)
            }
        }
    }


    private fun showHideLoading(isShow: Boolean) {
        try {
            if (isShow) {
                dialogLoading.show(
                    supportFragmentManager.beginTransaction().remove(dialogLoading),
                    DialogProgress::class.java.name
                )
            } else {
                dialogLoading.dismiss()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * Listener in onResume because unregisterReceiver will call in onStop
     * */
    fun registerBroadcastNotify(result: (Unit) -> Unit) {
        broadcastNotify = BroadcastRequestSign()
        broadcastNotify?.requestSignListener = {
            result.invoke(Unit)
        }
        ContextCompat.registerReceiver(
            this,
            broadcastNotify,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        keyboardHeightProvider.onResume()
    }


    fun unRegisterBroadcastRequestSign() {
        try {
            unregisterReceiver(broadcastNotify)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        unRegisterBroadcastRequestSign()
    }

    fun logout() {
        try {
            cleanDataLogin()
            viewModel.clearDataLogin()
            if (this !is SplashActivity) {
                openSplashScreen(isCleanTop = true)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun cleanDataLogin() {
        GlobalValue.clearUserData()
        globalViewModel.cleanData()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        try {
            FamChatManager.instance().startTimerLogout()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            keyboardHeightProvider.removeKeyboardListener(this)
            showHideLoading(false)
            unRegisterBroadcastRequestSign()
            unregisterReceiver(smsVerificationReceiver)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun startBroadcastSms(result: (String) -> Unit) {
        this.otpListener = result
        try {
            unregisterReceiver(smsVerificationReceiver)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        smsVerificationReceiver.intentListener = { intent ->
            startActivityForResult(intent, SMS_CONSENT_REQUEST)
        }
        val smsIntentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        ContextCompat.registerReceiver(
            this,
            smsVerificationReceiver,
            smsIntentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        SmsRetriever.getClient(this).startSmsUserConsent(null)
    }

    fun removeBroadcastSms() {
        try {
            unregisterReceiver(smsVerificationReceiver)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE) ?: ""
            val pattern: Pattern = Pattern.compile("\\b(\\d{4}|\\d{6})\\b")
            val matcher: Matcher = pattern.matcher(message)
            if (matcher.find()) {
                otpListener?.invoke(matcher.group(0))
            }
        }
    }

    fun addFragment(layoutId: Int, fragment: Fragment, title: String? = null) {
        try {
            hideSoftKeyboard()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val tag = title ?: fragment::class.java.name
            val existingFragment = supportFragmentManager.findFragmentByTag(tag)
            if (existingFragment == null) {
                fragmentTransaction.setCustomAnimations(
                    R.anim.pull_in_right,
                    R.anim.push_out_left,
                    R.anim.pull_in_left,
                    R.anim.push_out_right
                )
                fragmentTransaction.add(layoutId, fragment, tag)
                fragmentTransaction.addToBackStack(tag)
                fragmentTransaction.commitAllowingStateLoss()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun clearAllFragment() {
        supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.executePendingTransactions()
    }

    fun openScreenAfterLogout(openScreenName: String) {
        try {
            cleanDataLogin()
            if (this !is SplashActivity) {
                openSplashScreen(openScreenName = openScreenName, isCleanTop = true)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}

