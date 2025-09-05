package com.example.famchat.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.famchat.databinding.FcLayoutVerifyOtpBinding

class LayoutVerifyOTP @JvmOverloads constructor(
    context: Context,
    var attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var binding: FcLayoutVerifyOtpBinding

    init {
        initView()
    }

    private fun initView() {
        binding = FcLayoutVerifyOtpBinding.inflate(LayoutInflater.from(context), this, true)
//
//        attrs?.let {
//            val a: TypedArray =
//                context.obtainStyledAttributes(it, R.styleable.LayoutLogoAndDescription)
//            a.recycle()
//        }

        binding.edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = (s?.length ?: 0) >= 6

                if (isValid) {
                    binding.btnVerify.stagePrimary()
                } else {
                    binding.btnVerify.stageDisable()
                }
            }


            override fun afterTextChanged(s: Editable?) {}
        })


    }

    fun getOTP(): String {
        return binding.edt.text.toString()
    }

}
