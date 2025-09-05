package com.example.famchat.activity.registerAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

import com.example.famchat.activity.BaseActivity
import com.example.famchat.databinding.FcActivityRegisterAccountBinding
import com.example.famchat.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.widget.Toast
import com.example.famchat.activity.login.LoginActivity
import com.example.famchat.extensions.findActivity
import com.google.firebase.auth.FirebaseAuth


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

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
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
        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val name = binding.edtUsername.getText()
            val email = binding.edtEmail.getText()
            val password = binding.edtPassword.getText()
            val confirm = binding.edtConfirmPassword.getText()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirm) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerEmailPassword(name, email, password)
        }
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

private fun RegisterAccountActivity.backToLogin() {
    LoginActivity.start(this, false, null, "")
    finish()
}

private fun RegisterAccountActivity.registerEmailPassword(name: String, email: String, password: String) {
    firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                backToLogin()
            } else {
                Toast.makeText(this, task.exception?.message ?: "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }
        }
}