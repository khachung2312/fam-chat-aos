package com.example.famchat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.famchat.R
import com.example.famchat.databinding.FcLayoutHeaderBinding


class LayoutHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = FcLayoutHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    enum class Tab { MESSAGE, CALLS, CONTACTS, SETTINGS }

    var onLeftClick: (() -> Unit)? = null
    var onRightClick: (() -> Unit)? = null

    init {
        binding.btnLeft.setOnClickListener { onLeftClick?.invoke() }
        binding.btnRight.setOnClickListener { onRightClick?.invoke() }
    }

    /**
     * Cập nhật UI theo từng tab
     */
    fun setHeaderForTab(tab: Tab) {
        when (tab) {
            Tab.MESSAGE -> {
                binding.tvTitle.text = context.getString(R.string.fc_message)
                binding.btnLeft.setImageResource(R.drawable.fc_ic_search)
                binding.btnLeft.visibility = View.VISIBLE
                binding.imgAvatar.visibility = View.VISIBLE
                binding.btnRight.visibility = View.GONE

            }

            Tab.CALLS -> {
                binding.tvTitle.text = context.getString(R.string.fc_calls)
                binding.btnLeft.setImageResource(R.drawable.fc_ic_search)
                binding.btnLeft.visibility = View.VISIBLE
                binding.btnRight.setImageResource(R.drawable.fc_ic_call_user)
                binding.btnRight.visibility = View.VISIBLE
                binding.imgAvatar.visibility = View.GONE
            }

            Tab.CONTACTS -> {
                binding.tvTitle.text = context.getString(R.string.fc_contacts)
                binding.btnLeft.setImageResource(R.drawable.fc_ic_search)
                binding.btnLeft.visibility = View.VISIBLE
                binding.btnRight.setImageResource(R.drawable.fc_ic_user_add)
                binding.btnRight.visibility = View.VISIBLE
                binding.imgAvatar.visibility = View.GONE
            }

            Tab.SETTINGS -> {
                binding.tvTitle.text = context.getString(R.string.fc_settings)
                binding.btnLeft.visibility = View.VISIBLE
                binding.btnLeft.setImageResource(R.drawable.fc_ic_back)
                binding.btnRight.visibility = View.GONE
                binding.imgAvatar.visibility = View.GONE
            }
        }
    }
}