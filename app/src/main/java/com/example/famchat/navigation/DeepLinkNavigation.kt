package com.example.famchat.navigation

import android.net.Uri
import com.example.famchat.activity.BaseActivity
import com.example.famchat.config.Constants

/**
 * open screen app by name, not token
 * */
fun BaseActivity<*, *>.openScreenByNameNotLogin(data: Uri?, screenName: String?): Boolean {
    return when (screenName) {
        Constants.ScreenName.SCREEN_LOGIN -> {
            openLoginScreen(isCleanTop = true)
            true
        }

//        Constants.ScreenName.SCREEN_REGISTER_ACCOUNT -> {
//            openRegisterAccount(data)
//            true
//        }
//
//        Constants.ScreenName.SEARCH_ORDER -> {
//            openOrderLookup()
//            true
//        }

        else -> {
            false
        }
    }
}

/**
 * open screen app by name, must token
 * */
fun BaseActivity<*, *>.openScreenByName(screenName: String?, data: Uri? = null): Boolean {
    if (openScreenByNameNotLogin(intent.data, screenName)) {
        return true
    }
    return when (screenName) {
        Constants.ScreenName.LIST_REQUEST_SIGN -> {
//            openRequestSign()
            true
        }

        Constants.ScreenName.BUY_PACKAGE -> {
//            openRegisterVas()
            true
        }

        Constants.ScreenName.REVOKE_CTS -> {
//            openRevokeCts(
//                orderId = intent?.data?.getQueryParameter("order_id"),
//                requestId = intent?.data?.getQueryParameter("request_id"),
//                itemReason = ItemReason(
//                    reasonId = intent?.data?.getQueryParameter("reasonId"),
//                    reasonCode = intent?.data?.getQueryParameter("reasonCode"),
//                    name = intent?.data?.getQueryParameter("reasonName"),
//                    reasonType = intent?.data?.getQueryParameter("reasonType"),
//                )
//            )
            true
        }

        Constants.ScreenName.EXTENSION_CTS -> {
//            openScreenExtensionPackage(
//                orderId = intent?.data?.getQueryParameter("order_id"),
//                requestId = intent?.data?.getQueryParameter("request_id")
//            )
            true
        }

        Constants.ScreenName.CHANGE_PACKAGE -> {
//            openChangePackage(
//                orderId = intent?.data?.getQueryParameter("order_id"),
//                requestId = intent?.data?.getQueryParameter("request_id")
//            )
            true
        }

        Constants.ScreenName.CREATE_SIGNATURE -> {
//            openCreateSignature()
            true
        }

        Constants.ScreenName.SYSTEM_LOCKUP -> {
//            openSystemIntegration()
            true
        }

        Constants.ScreenName.NOTIFICATION -> {
//            val mData = intent?.data ?: data
//            val notifyId = mData?.getQueryParameter("id").safeParseInt(-1)
//            openManagerNotification(notifyId) {
//                (viewModel as? MainViewModel)?.getCountNotifyUnRead()
//            }
            true
        }

        Constants.ScreenName.ACCOUNT_INFO -> {
//            openUserProfile()
            true
        }

        Constants.ScreenName.REPORT -> {
//            openScreenReport()
            true
        }

        Constants.ScreenName.ACCEPTANCE -> {
//            checkAcceptanceCtsNotData()
            true
        }

        Constants.ScreenName.LIST_CTS -> {
//            openListCtsScreen()
            true
        }

        Constants.ScreenName.MANAGE_DEVICE -> {
//            openListDevice()
            true
        }

        Constants.ScreenName.CREATE_REQUEST -> {
//            openCreateRequestSign(this.viewModel)
            true
        }

        Constants.ScreenName.REGISTER_DEVICE -> {
//            (this as? MainActivity)?.requestRegisterUnregisterDevice()
            true
        }

        Constants.ScreenName.SETTING -> {
//            openSetting()
            true
        }

        Constants.ScreenName.CHANGE_PASSWORD -> {
//            openChangePasswordScreen()
            true
        }

        Constants.ScreenName.APP_INFO -> {
//            openVersionInfo()
            true
        }

        Constants.ScreenName.INVITE_FRIEND -> {
//            openInviteFriends()
            true
        }

        Constants.ScreenName.CHANGE_PHONE -> {
//            openChangeInfo(
//                orderId = intent?.data?.getQueryParameter("order_id"),
//                requestId = intent?.data?.getQueryParameter("request_id"),
//                phoneChange = intent?.data?.getQueryParameter("phone_change"),
//                emailChange = intent?.data?.getQueryParameter("email_change")
//            )
            true
        }

        else -> {
            false
        }
    }
}

/**
 * Handler screen open by key from deeplink/notification not login
 * */
fun BaseActivity<*, *>.openScreenNotLogin(data: Uri?): Boolean {
    val keyAction = try {
        data!!.pathSegments[0]
    } catch (ex: Exception) {
        null
    }
    return when (keyAction) {
        Constants.HostDeepLink.COMPLETE_ORDER_REGISTER -> {
            val orderCode = data?.getQueryParameter("orderCode")
//            openSelectRegisterVas(orderCode)
            true
        }

        Constants.HostDeepLink.SCREEN_NAVIGATION -> {
            val screenName = data?.getQueryParameter("name")
            openScreenByNameNotLogin(data, screenName)
        }

        else -> {
            false
        }
    }
}
