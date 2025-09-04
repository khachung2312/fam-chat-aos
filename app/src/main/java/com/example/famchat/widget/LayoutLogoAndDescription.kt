package com.example.famchat.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.famchat.databinding.FcLayoutLogoAndDescriptionBinding
import com.example.famchat.R


class LayoutLogoAndDescription @JvmOverloads constructor(
    context: Context,
    var attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: FcLayoutLogoAndDescriptionBinding

    init {
        initView()
    }

    private fun initView() {
        binding = FcLayoutLogoAndDescriptionBinding.inflate(LayoutInflater.from(context), this, true)
        if (isInEditMode) return

        attrs?.let {
            val a: TypedArray =
                context.obtainStyledAttributes(it, R.styleable.LayoutLogoAndDescription)

            val logoResId = a.getResourceId(R.styleable.LayoutLogoAndDescription_iv_logo, 0)
            if (logoResId != 0) {
                binding.ivLogo.setImageResource(logoResId)
            }

            val titleText = a.getString(R.styleable.LayoutLogoAndDescription_textTitle)
            if (!titleText.isNullOrEmpty()) {
                binding.tvTitle.text = titleText
            }

            val descText = a.getString(R.styleable.LayoutLogoAndDescription_textDescription)
            if (!descText.isNullOrEmpty()) {
                binding.tvDescription.text = descText
            }

            a.recycle()
        }
    }

    fun setLogo(resId: Int) {
        binding.ivLogo.setImageResource(resId)
    }

    fun setTitle(text: String) {
        binding.tvTitle.text = text
    }

    fun setDescription(text: String) {
        binding.tvDescription.text = text
    }



}
