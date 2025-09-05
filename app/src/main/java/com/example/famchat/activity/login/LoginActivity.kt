package com.example.famchat.activity.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.example.famchat.BuildConfig
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity
import com.example.famchat.activity.splash.SplashActivity
import com.example.famchat.config.Constants
import com.example.famchat.config.LanguageSetting
import com.example.famchat.databinding.CaActivityLoginBinding
import com.example.famchat.dialog.showDialogNotify
import com.example.famchat.extensions.findActivity
import com.example.famchat.extensions.ifNotNullOrEmpty
import com.example.famchat.extensions.loadImage
import com.example.famchat.extensions.setSafeOnClickListener
import com.example.famchat.extensions.setStatusBarHomeTransparent
import com.example.famchat.extensions.spanText
import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.model.reponse.LoginResponse
import com.example.famchat.navigation.openMainScreen
import com.example.famchat.navigation.openRegisterAccountActivity
import com.example.famchat.navigation.openScreenByName
import com.example.famchat.navigation.openScreenNotLogin
import com.example.famchat.utils.NAME
import com.example.famchat.utils.PreferencesUtils
import com.example.famchat.utils.RESTORE_FAIL
import com.example.famchat.utils.RESTORE_USER_NOTFOUND
import com.example.famchat.viewmodel.AuthViewModel
import com.example.famchat.viewmodel.GlobalValue
import com.example.famchat.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : BaseActivity<CaActivityLoginBinding, AuthViewModel>(), View.OnClickListener {

    companion object {
        const val OPEN_SCREEN_DEEPLINK = "OPEN_SCREEN_DEEPLINK"
        fun start(
            context: Context,
            isCleanTop: Boolean = false,
            data: Uri?,
            openScreenName: String = "",
        ) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.data = data
            val screenName = intent.data?.getQueryParameter(NAME) ?: ""
            screenName.ifNotNullOrEmpty { intent.putExtra(OPEN_SCREEN_DEEPLINK, it) }
            if (isCleanTop) {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra(SplashActivity.OPEN_SCREEN_NAME, openScreenName)
            context.startActivity(intent)
        }
    }

    private var fingerKey: String = ""
    private val openScreenName by lazy {
        intent.getStringExtra(SplashActivity.OPEN_SCREEN_NAME) ?: ""
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    lateinit var firebaseAuth: FirebaseAuth

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handlerIntentDeeplink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun initView() {
        setStatusBarHomeTransparent()
        initGoogleSignIn()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        fingerKey = PreferencesUtils.getString(Constants.Finger.FINGER_PRINT)
        cleanDataLogin()
        handlerIntentDeeplink(intent)

    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("191660585296-8hqu6d4qij9o0vsul4k7lru34pv4nc38.apps.googleusercontent.com")
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun handlerIntentDeeplink(intent: Intent?) {
        if (openScreenNotLogin(intent?.data)) {
            intent?.data = null
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun initData() {
        globalViewModel
        globalViewModel.userLogin = null
        openScreenName.ifNotNullOrEmpty { openScreenByName(it) }
    }


    override fun initListener() {
        binding.ivBack.setOnClickListener{
            finish()
        }
        binding.edtPassword.onActionDoneListener = {
            if (binding.btnLogin.isEnabled) {
                binding.btnLogin.callOnClick()
            }
        }
        binding.cbSaveAccount.setOnCheckedChangeListener { _, isChecked ->
            if (TextUtils.isEmpty(fingerKey)) {
                PreferencesUtils.putBoolean(
                    Constants.Preferences.SHOW_SUGGEST_ACCOUNT,
                    isChecked
                )
            }
        }
        binding.edtUsername.onTextChangeListener = {
            validateEnableBtnLogin()
        }
        binding.edtPassword.onTextChangeListener = {
            validateEnableBtnLogin()
        }
        binding.btnLogin.setSafeOnClickListener(this::onClick)
        binding.btnLogin.setOnClickListener {
            val email = binding.edtUsername.getText()
            val password = binding.edtPassword.getText()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        handlerScreenOpen()
                    } else {
                        Toast.makeText(this, task.exception?.message ?: "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.tvForgotPassword.setSafeOnClickListener(this::onClick)

        binding.tvForgotPassword.setSafeOnClickListener(this::onClick)
    }

    override fun onClick(view: View?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


    private fun validateDataLogin(data: BaseResponse<LoginResponse>) {
        if (data.success) {
            data.result?.let {

            }
            PreferencesUtils.putString(
                Constants.Preferences.ACCOUNT_LAST_LOGIN,
                binding.edtUsername.getText()
            )
            handlerScreenOpen()
        }
    }


    private fun handlerScreenOpen() {
        GlobalValue.userData.hasValidateCloudCA = false
        openMainScreen()
        finish()
    }

    private fun validateEnableBtnLogin() {
        if (!TextUtils.isEmpty(binding.edtUsername.getText())
            && !TextUtils.isEmpty(binding.edtPassword.getText())
        ) {
            binding.btnLogin.stagePrimary()
        } else {
            binding.btnLogin.stageDisable()
        }
    }

    override fun getViewBinding(): CaActivityLoginBinding {
        return CaActivityLoginBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }



}