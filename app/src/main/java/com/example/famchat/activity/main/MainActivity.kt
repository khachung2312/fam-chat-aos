package com.example.famchat.activity.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity
import com.example.famchat.activity.main.fragment.CallsFragment
import com.example.famchat.activity.main.fragment.ContactsFragment
import com.example.famchat.activity.main.fragment.MessageFragment
import com.example.famchat.activity.main.fragment.SettingsFragment
import com.example.famchat.databinding.FcActivityMainBinding
import com.example.famchat.dialog.base.BaseDialog
import com.example.famchat.extensions.setStatusBarHomeTransparent
import com.example.famchat.navigation.openScreenByName
import com.example.famchat.viewmodel.MainViewModel
import com.example.famchat.widget.LayoutHeaderView
import java.util.Timer

class MainActivity : BaseActivity<FcActivityMainBinding, MainViewModel>(), View.OnClickListener {

    companion object {
        const val MAIN_OPEN_SINGLE_SCREEN = "MAIN_OPEN_SINGLE_SCREEN"
        fun start(context: Context, data: Uri?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.data = data
            context.startActivity(intent)
        }
    }

    private var isCheckVersionAppPassed = false

    private lateinit var timerStartAngleChange: Timer

    private var dialog: BaseDialog<*, *>? = null
    private var pathFileShare: String? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handlerIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            switchFragment(MessageFragment())
            // Đồng thời highlight tab MESSAGE
            binding.customNavigationView.binding.bottomNavigation.selectedItemId =
                R.id.menu_message
        }

    }

    override fun initView() {
        setStatusBarHomeTransparent()
    }

    override fun initData() {

    }


    private fun handlerIntent(intent: Intent?): Boolean {
        intent?.data?.getQueryParameter("name")?.let {
            return openScreenByName(it, data = intent.data)
        }
        if (!pathFileShare.isNullOrEmpty()) return true
        return false
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        isCheckVersionAppPassed = false
    }

    override fun initViewModel() {
        super.initViewModel()
        globalViewModel.userInformation.observe(this) {

        }


        lifecycleScope.launchWhenStarted {
            viewModel.streamedText.collect { chunk ->

            }
        }
    }

    override fun initListener() {
        binding.customNavigationView.onTabSelected = { tab ->
            // 1. Chuyển fragment
            when (tab) {
                LayoutNavigationView.Tab.MESSAGE -> switchFragment(MessageFragment())
                LayoutNavigationView.Tab.CALLS -> switchFragment(CallsFragment())
                LayoutNavigationView.Tab.CONTACTS -> switchFragment(ContactsFragment())
                LayoutNavigationView.Tab.SETTINGS -> switchFragment(SettingsFragment())
            }

            // 2. Cập nhật header
            binding.layoutHeader.setHeaderForTab(
                when(tab) {
                    LayoutNavigationView.Tab.MESSAGE -> LayoutHeaderView.Tab.MESSAGE
                    LayoutNavigationView.Tab.CALLS -> LayoutHeaderView.Tab.CALLS
                    LayoutNavigationView.Tab.CONTACTS -> LayoutHeaderView.Tab.CONTACTS
                    LayoutNavigationView.Tab.SETTINGS -> LayoutHeaderView.Tab.SETTINGS
                }
            )
        }


    }



    override fun onClick(view: View?) { }

    override fun onBackPressed() { super.onBackPressed() }

    override fun getViewBinding(): FcActivityMainBinding {
        return FcActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

}