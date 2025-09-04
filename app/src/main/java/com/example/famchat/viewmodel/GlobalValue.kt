package com.example.famchat.viewmodel

import com.example.famchat.config.Constants
import com.example.famchat.model.UserData
import com.example.famchat.model.reponse.ConfigDataResponse
import com.example.famchat.utils.PreferencesUtils


object GlobalValue {

    /** User information */
    var userData = UserData()

    var sharedKey: String = ""

    var currentLanguage: String
        get() {
            return PreferencesUtils.getString(
                Constants.Preferences.KEY_LANGUAGE,
                Constants.DefaultValue.LANGUAGE_DEFAULT
            )
        }
        set(value) {}

    /** all config of app */
    var appConfigData: ConfigDataResponse? = null

    var showRecommendChangePw: Boolean = false

    fun clearUserData() {
        userData = UserData()
    }

    var listRevokeAccpeptCts = arrayListOf<String>()
    var reloadListCtsHome = false
}