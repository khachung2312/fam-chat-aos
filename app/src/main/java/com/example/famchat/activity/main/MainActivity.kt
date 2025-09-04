package com.example.famchat.activity.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity
import com.example.famchat.databinding.CaActivityMainBinding
import com.example.famchat.dialog.base.BaseDialog
import com.example.famchat.extensions.setSafeOnClickListener
import com.example.famchat.extensions.setStatusBarHomeTransparent
import com.example.famchat.navigation.openScreenByName
import com.example.famchat.viewmodel.MainViewModel
import java.util.Timer

class MainActivity : BaseActivity<CaActivityMainBinding, MainViewModel>(), View.OnClickListener {

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
    }

    override fun initView() {
        setStatusBarHomeTransparent()
//        binding.mainLayout.rcvFunction.adapter = homeAdapter
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
//        globalViewModel.userRegisterLiveData.observe(this) { isRegister ->
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.streamedText.collect { chunk ->
//                val textView = AppCompatTextView(baseContext).apply {
//                    text = chunk
//                    setTextColor(Color.BLACK) // hoặc tùy chỉnh
//                    textSize = 16f
//                    setPadding(8, 8, 8, 8)
//                }
//
//                // Thêm vào LinearLayout
//                binding.mainLayout.linearLayout.addView(textView)

                binding.mainLayout.tvView.append(chunk + "\n")

                binding.mainLayout.nestedScrollView.post {
                    binding.mainLayout.nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    override fun initListener() {
        binding.mainLayout.ivMenu.setSafeOnClickListener(this::onClick)
        binding.customNavigationView.onTabSelected = { tab ->
            when (tab) {
                LayoutNavigationView.Tab.HOME -> switchFragment(HomeFragment())
                LayoutNavigationView.Tab.CHAT -> switchFragment(ChatFragment())
                LayoutNavigationView.Tab.SETTINGS -> switchFragment(SettingsFragment())
            }
        }

//        binding.mainLayout.tvRecharge.setSafeOnClickListener {
//            binding.mainLayout.layoutBalance.openRechargeScreen()
//        }
//
//        binding.mainLayout.ivNotification.setSafeOnClickListener {
//            openScreenByName(Constants.ScreenName.NOTIFICATION)
//        }

        binding.mainLayout.btnLogin.setSafeOnClickListener {
            viewModel.sendChatMessage("tôi muốn tìm việc làm tester tại Hà Nội")
        }
    }



    override fun onClick(view: View?) { }

    override fun onBackPressed() { super.onBackPressed() }

    override fun getViewBinding(): CaActivityMainBinding {
        return CaActivityMainBinding.inflate(LayoutInflater.from(this))
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