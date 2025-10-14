package com.example.famchat.widget

import android.content.Context
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.famchat.R
import com.example.famchat.databinding.FcLayoutCustomEditextBinding
import com.example.famchat.extensions.safeParseLong
import com.example.famchat.extensions.setSafeOnClickListener
import com.example.famchat.extensions.toHtml
import com.example.famchat.extensions.toMoneyValue


class CustomEditText(context: Context, var attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private lateinit var binding: FcLayoutCustomEditextBinding

    var onTextChangeListener: ((String) -> Unit)? = null
    var onTextChanged: (String) -> Unit = {}
    var onTextFocusListener: ((Boolean) -> Unit)? = null
    var onTextEndClickListener: ((String) -> Unit)? = null
    var onIconEndClickListener: (() -> Unit)? = null
    var onActionDoneListener: ((String) -> Unit)? = null
    var onClickViewMaskListener: (() -> Unit)? = null
    private var inputType = 0
    private var showClearButton = true
    private var multiLine = false
    private var maxLength = 200
    var currentTextMoney: String = ""
    var isEnableIconEnd: Boolean = false

    val editText by lazy {
        binding.edtInput
    }

    init {
        initView()
        intListener()
    }

    fun enableEditText(isEnable: Boolean) {
        binding.edtInput.isEnabled = isEnable
        binding.ivClear.visibility =
            if (isEnable && !TextUtils.isEmpty(binding.edtInput.text) && showClearButton) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    fun isEnableEditText() = binding.edtInput.isEnabled && binding.root.isVisible

    fun getText(): String {
        return (binding.edtInput.text ?: "").trim().toString()
    }

    fun setText(input: String?, moveCursorLast: Boolean = false) {
        binding.edtInput.setText(input ?: "")
        if (moveCursorLast) {
            binding.edtInput.setSelection(binding.edtInput.length())
        }
    }

    fun setError(error: String, requestFocus: Boolean = true) {
        if (TextUtils.isEmpty(error)) {
            binding.tvError.visibility = View.GONE
        } else {
            binding.tvError.text = error
            binding.tvError.visibility = View.VISIBLE
            if (requestFocus) {
                binding.edtInput.requestFocus()
            }
        }
    }

    fun setTextEnd(input: String) {
        if (TextUtils.isEmpty(input)) {
            binding.tvEnd.text = ""
            binding.tvEnd.visibility = View.GONE
        } else {
            binding.tvEnd.text = input.toHtml()
            binding.tvEnd.visibility = View.VISIBLE
        }
    }

    fun setHint(hint: String) {
        binding.edtInput.hint = hint
        binding.textInputLayout.hint = hint
    }

    fun setHintForce(hint: String) {
        val text = "$hint<font color='red'>*</font>"
        binding.textInputLayout.hint = text.toHtml()
    }

    fun setEndIcon(resource: Int) {
        binding.ivEnd.apply {
            setImageResource(resource)
            visibility = if (resource == 0) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
    fun setMaxLength(length: Int) {
        binding.edtInput.filters = arrayOf<InputFilter>(LengthFilter(length))
    }

    private fun initView() {
        binding = FcLayoutCustomEditextBinding.inflate(LayoutInflater.from(context), this, true)
        val attrArr = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0)

        attrArr.getDrawable(R.styleable.CustomEditText_endIcon)?.let {
            binding.ivEnd.apply {
                setImageDrawable(it)
                visibility = View.VISIBLE
            }
        } ?: run {
            binding.ivEnd.visibility = View.GONE
        }

        attrArr.getBoolean(R.styleable.CustomEditText_clickViewEnable, false).let {
            binding.frmClick.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        attrArr.getBoolean(R.styleable.CustomEditText_showClearButton, true).let {
            showClearButton = it
        }

        attrArr.getBoolean(R.styleable.CustomEditText_multiLine, false).let {
            multiLine = it
        }

        attrArr.getInt(R.styleable.CustomEditText_maxLength, 200).let {
            maxLength = it
            binding.edtInput.filters = arrayOf<InputFilter>(LengthFilter(it))
        }

        attrArr.getString(R.styleable.CustomEditText_hint)?.let {
            binding.textInputLayout.hint = it.toHtml()
        }

        attrArr.getString(R.styleable.CustomEditText_hintForce)?.let {
            val text = "$it<font color='red'>*</font>"
            binding.textInputLayout.hint = text.toHtml()
        }

        attrArr.getString(R.styleable.CustomEditText_endText)?.let {
            binding.tvEnd.text = it
            binding.tvEnd.visibility = View.VISIBLE
        }

        attrArr.getBoolean(R.styleable.CustomEditText_enable, true).let {
            enableEditText(it)
        }

        binding.edtInput.setTextColor(
            attrArr.getColor(
                R.styleable.CustomEditText_textColor,
                context.resources.getColor(R.color.color_text_default)
            )
        )
        inputType = attrArr.getInteger(R.styleable.CustomEditText_inputType, 0)
        when (inputType) {
            0 -> {
                /** Text */
                binding.edtInput.inputType =
                    if (multiLine) {
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
                    } else {
                        InputType.TYPE_CLASS_TEXT
                    }
            }

            1, 3 -> {
                /** Number */
                binding.edtInput.inputType =
                    if (multiLine) {
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_NUMBER
                    } else {
                        InputType.TYPE_CLASS_NUMBER
                    }
            }

            2 -> {
                /** Password */
                binding.edtInput.transformationMethod = PasswordTransformationMethod()
            }

        }

        val textStyle: Int = when (attrArr.getInteger(R.styleable.CustomEditText_textStyle, 0)) {
            0 -> {
                R.style.TextStyleNormal
            }

            1 -> {
                R.style.TextStyleMedium
            }

            2 -> {
                R.style.TextStyleBold
            }

            else -> {
                R.style.TextStyleNormal
            }
        }
        binding.edtInput.setTextAppearance(textStyle)

        when (attrArr.getInteger(R.styleable.CustomEditText_imeOptions, 0)) {
            0 -> {
                binding.edtInput.imeOptions = EditorInfo.IME_ACTION_NEXT
            }

            1 -> {
                binding.edtInput.imeOptions = EditorInfo.IME_ACTION_DONE
            }
        }

        val isInputDate = attrArr.getBoolean(R.styleable.CustomEditText_inputDate, false)
        if (isInputDate) {
            binding.edtInput.inputType = InputType.TYPE_CLASS_DATETIME
        }

    }

    private fun intListener() {
        binding.frmClick.setSafeOnClickListener {
            if (!binding.edtInput.isEnabled) {
                return@setSafeOnClickListener
            }
            onClickViewMaskListener?.invoke()
        }
        binding.edtInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onActionDoneListener?.invoke(binding.edtInput.text.toString())
                true
            }
            false
        }
        binding.tvEnd.setSafeOnClickListener {
            onTextEndClickListener?.invoke(binding.tvEnd.text.toString())
        }
        binding.ivEnd.setSafeOnClickListener {
            onIconEndClickListener?.invoke()
        }
        binding.ivClear.setSafeOnClickListener {
            setText("")
        }
        binding.ivPassword.setSafeOnClickListener {
            binding.edtInput.transformationMethod?.let {
                binding.edtInput.transformationMethod = null
                binding.ivPassword.setImageResource(R.drawable.ca_ic_hide_pass)
            } ?: run {
                binding.edtInput.transformationMethod = PasswordTransformationMethod()
                binding.ivPassword.setImageResource(R.drawable.ca_ic_show_pass)
            }
            binding.edtInput.setSelection(binding.edtInput.length())
        }
        binding.edtInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // beforeTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0.toString())
                handlerInputMoney(p0.toString(), this)
                binding.tvError.visibility = View.GONE
                if (!binding.edtInput.isEnabled) {
                    return
                }
                binding.ivClear.visibility = if (TextUtils.isEmpty(p0)) {
                    View.GONE
                } else if (!showClearButton) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                if (inputType == 2) {
                    binding.ivPassword.visibility = if (TextUtils.isEmpty(p0)) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
                onTextChangeListener?.invoke(p0.toString())
                binding.ivEnd.isVisible = isEnableIconEnd && p0?.isEmpty() == true
            }

            override fun afterTextChanged(p0: Editable?) {
                // afterTextChanged
            }

        })

        binding.edtInput.setOnFocusChangeListener { v, hasFocus ->
            onTextFocusListener?.invoke(hasFocus)
            if (hasFocus) {
                binding.line.setBackgroundColor(resources.getColor(R.color.fc_color_primary))
            } else {
                binding.line.setBackgroundColor(resources.getColor(R.color.color_ADB5BD))
            }
        }
    }

    private fun handlerInputMoney(input: String, watcher: TextWatcher) {
        if (inputType == 3 && input != currentTextMoney && input.length <= maxLength) {
            binding.edtInput.removeTextChangedListener(watcher)
            if (TextUtils.isEmpty(input)) {
                binding.edtInput.setText(input)
                currentTextMoney = input
            } else {
                val current = input.replace(".", "")
                val format = current.safeParseLong().toMoneyValue("")
                currentTextMoney = format
                binding.edtInput.setText(format)
                binding.edtInput.setSelection(format.length)
            }
            binding.edtInput.addTextChangedListener(watcher)
        }
    }

}