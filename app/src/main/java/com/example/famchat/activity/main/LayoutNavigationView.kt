package com.example.famchat.activity.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.famchat.R
import com.example.famchat.databinding.CaLayoutBottomNavBinding

class LayoutNavigationView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs), View.OnClickListener {

    private lateinit var binding: CaLayoutBottomNavBinding

    var onTabSelected: ((Tab) -> Unit)? = null

    enum class Tab { HOME, CHAT, SETTINGS }

    init {
        initView()
        initListener()
    }

    private fun initView() {
        binding = CaLayoutBottomNavBinding.inflate(LayoutInflater.from(context), this, true)
        binding.bottomNavigation.menu.clear()
        binding.bottomNavigation.inflateMenu(R.menu.ca_bottom_nav_menu)
    }

    private fun initListener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> onTabSelected?.invoke(Tab.HOME)
                R.id.menu_chat -> onTabSelected?.invoke(Tab.CHAT)
                R.id.menu_settings -> onTabSelected?.invoke(Tab.SETTINGS)
            }
            true
        }
    }

    override fun onClick(v: View?) {}
}