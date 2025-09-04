package com.example.mimiAlpha.activity.registerAccount

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.example.famchat.activity.BaseActivity
import com.example.famchat.databinding.FcActivityRegisterAccountBinding
import com.example.famchat.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class RegisterAccountActivity : BaseActivity<FcActivityRegisterAccountBinding, AuthViewModel>(),
    View.OnClickListener {

    companion object {
        fun start(
            context: Context
        ) {
            val intent = Intent(context, RegisterAccountActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


    }


    override fun onResume() {
        super.onResume()
    }


    override fun initData() {
    }

    override fun initListener() {


    }

    private var resendTimer: CountDownTimer? = null

    override fun onClick(view: View?) {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun getViewBinding(): FcActivityRegisterAccountBinding {
        return FcActivityRegisterAccountBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }


}