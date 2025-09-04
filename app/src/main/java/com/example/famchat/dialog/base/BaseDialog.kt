package com.example.famchat.dialog.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.example.famchat.activity.BaseActivity
import com.example.famchat.extensions.setSafeOnClickListener
import com.example.famchat.extensions.toHtml
import com.example.famchat.extensions.toast
import kotlin.collections.HashMap

abstract class BaseDialog<BD : ViewBinding, B : BuilderDialog> : DialogFragment() {

    protected abstract val viewBinding: BD
    lateinit var binding: BD
    lateinit var builder: B
    private var countDownDismiss: CountDownTimer? = null
    protected val baseActivity: BaseActivity<*, *>
        protected get() = activity as BaseActivity<*, *>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = viewBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            dialog?.let {
                val window = it.window
                window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                )
                val windowParams = window.attributes
                window.setLayout(
                    getWidth(context) / 100 * 90,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                windowParams.dimAmount = 0.7f
                window.attributes = windowParams
                it.setCancelable(builder.cancelable)
                it.setCanceledOnTouchOutside(builder.canOnTouchOutside)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            initView()
            initListener()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getWidth(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        val windowmanager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowmanager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun toast(message: String) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).toast(message)
        }
    }

    private fun startCountdownTimer(time: Long) {
        countDownDismiss?.let {
            it.cancel()
        }
        countDownDismiss = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //onTick
            }

            override fun onFinish() {
                dismiss()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownDismiss?.let {
            it.cancel()
        }
    }

    open fun initView() {
        if (!::builder.isInitialized) {
            return
        }
        title?.let {
            it.text = builder.title.orEmpty().toHtml()
        }

        positiveButton?.let {
            if (!builder.positiveButton.isNullOrEmpty()) {
                it.text = builder.positiveButton
                it.setSafeOnClickListener {
                    handleClickPositiveButton(HashMap())
                }
            }
        }

        negativeButton?.let {
            if (!builder.negativeButton.isNullOrEmpty()) {
                it.text = builder.negativeButton
                it.setSafeOnClickListener {
                    handleClickNegativeButton()
                }
            }
        }

        message?.let {
            it.text = builder.message.orEmpty().toHtml()
        }

        if (builder.timeAutoDismiss > 0L) {
            startCountdownTimer(builder.timeAutoDismiss)
        }

    }

    protected abstract fun initListener()
    protected open val positiveButton: AppCompatButton?
        get() = null
    protected open val negativeButton: AppCompatButton?
        get() = null
    protected open val title: TextView?
        get() = null
    protected open val message: TextView?
        get() = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (::builder.isInitialized) {
            builder.onDismissListener?.invoke()
        }
    }

    protected open fun handleClickNegativeButton() {
        if (::builder.isInitialized) {
            builder.negativeButtonListener?.invoke(this)
        }
    }

    protected open fun handleClickPositiveButton(data: HashMap<String?, Any?>) {
        if (::builder.isInitialized) {
            builder.positiveButtonListener?.invoke(this, data)
        }
    }
}