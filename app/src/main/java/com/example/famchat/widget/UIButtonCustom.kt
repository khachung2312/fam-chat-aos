package com.example.famchat.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.famchat.R

class LibUIButtonCustom(context: Context, var attrs: AttributeSet?) :
    AppCompatButton(context, attrs) {

    init {
        initView()
    }

    private fun initView() {
        attrs?.let {
            val a: TypedArray =
                context.obtainStyledAttributes(it, R.styleable.LibLibUIButtonCustom)
            when (a.getInteger(R.styleable.LibLibUIButtonCustom_button_type, 1)) {
                0 -> {
                    applyStyle(R.style.ImageViewStyle1)
                    stageDisable()
                }
                1 -> {
                    stagePrimary()
                }
                2 -> {
                    stageWhite()
                }
                3 -> {
                    stageBlue()
                }
                4 -> {
                    stageStrokePrimary()
                }
                5 -> {
                    stageStrokeDisable()
                }
                6 -> {
                    stageStrokeBlack()
                }
                else -> {
                    stagePrimary()
                }
            }
        }
    }

    private fun applyStyle(styleResId: Int) {
        val typedArray = context.obtainStyledAttributes(styleResId, R.styleable.LibLibUIButtonCustom)

        // Apply background and textColor from the style
//        val backgroundRes = typedArray.getResourceId(R.styleable.LibLibUIButtonCustom_button_type, 0)
//        val textColor = typedArray.getColor(R.styleable.LibUIButtonCustom_android_textColor, Color.BLACK)

//        setBackgroundResource(backgroundRes)
//        setTextColor(textColor)

        typedArray.recycle()
    }

    fun stageDisable() {
        isEnabled = false
        setTextColor(resources.getColor(R.color.color_c1c7d3))
        setBackgroundResource(R.drawable.ca_btn_disable)
    }

    fun stagePrimary() {
        isEnabled = true
        setTextColor(Color.WHITE)
        setBackgroundResource(R.drawable.ca_btn_primary)
    }

    fun stageWhite() {
        isEnabled = true
        setTextColor(context.resources.getColor(R.color.color_44494D))
        setBackgroundResource(R.drawable.ca_btn_white)
    }

    fun stageBlue() {
        isEnabled = true
        setTextColor(Color.WHITE)
        setBackgroundResource(R.drawable.ca_btn_blue)
    }

    fun stageStrokePrimary() {
        isEnabled = true
        setTextColor(resources.getColor(R.color.ca_color_primary))
        setBackgroundResource(R.drawable.ca_btn_stroke_primary)
    }

    fun stageStrokeDisable() {
        isEnabled = false
        setTextColor(resources.getColor(R.color.color_c1c7d3))
        setBackgroundResource(R.drawable.ca_btn_stroke_disable)
    }

    fun stageStrokeBlack() {
        isEnabled = true
        setTextColor(resources.getColor(R.color.color_44494D))
        setBackgroundResource(R.drawable.ca_btn_stroke_black)
    }

}