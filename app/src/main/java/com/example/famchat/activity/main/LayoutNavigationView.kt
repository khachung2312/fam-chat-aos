package com.example.famchat.activity.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.famchat.R
import com.example.famchat.databinding.FcLayoutBottomNavBinding

class LayoutNavigationView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs), View.OnClickListener {

    private lateinit var binding: FcLayoutBottomNavBinding

    var onTabSelected: ((Tab) -> Unit)? = null

    enum class Tab { MESSAGE, CALLS, CONTACTS, SETTINGS }

    init {
        initView()
        initListener()
    }

    private fun initView() {
        binding = FcLayoutBottomNavBinding.inflate(LayoutInflater.from(context), this, true)
        binding.bottomNavigation.menu.clear()
        binding.bottomNavigation.inflateMenu(R.menu.ca_bottom_nav_menu)
    }

    private fun initListener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_message -> onTabSelected?.invoke(Tab.MESSAGE)
                R.id.menu_call -> onTabSelected?.invoke(Tab.CALLS)
                R.id.menu_contacts -> onTabSelected?.invoke(Tab.CONTACTS)
                R.id.menu_settings -> onTabSelected?.invoke(Tab.SETTINGS)
            }
            true
        }
    }



    override fun onClick(v: View?) {}
}