package com.example.famchat.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.famchat.R

import com.example.famchat.activity.BaseActivity
import com.example.famchat.databinding.FcActivityAuthBinding
import com.example.famchat.extensions.spanText
import com.example.famchat.navigation.openLoginScreen
import com.example.famchat.navigation.openRegisterAccountActivity
import com.example.famchat.viewmodel.AuthViewModel


class AuthActivity : BaseActivity<FcActivityAuthBinding, AuthViewModel>(),
    View.OnClickListener {

    companion object {
        fun start(
            context: Context
        ) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.tvLogin.spanText(
            textContent = getString(R.string.fc_hasAccountLogin),
            textHighLights = listOf(getString(R.string.fc_login))
        ) {
            openLoginScreen()
        }

    }


    override fun onResume() {
        super.onResume()
    }


    override fun initData() {
    }

    override fun initListener() {
        binding.btnRegister.setOnClickListener{
            openRegisterAccountActivity()
        }

        binding.tvLogin.setOnClickListener{
            openLoginScreen()
        }

    }

    private var resendTimer: CountDownTimer? = null

    override fun onClick(view: View?) {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun getViewBinding(): FcActivityAuthBinding {
        return FcActivityAuthBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }


}