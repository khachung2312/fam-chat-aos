package com.example.famchat.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.famchat.config.ApiErrorAction
import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.model.reponse.CheckDeepLinkResponse
import com.example.famchat.model.reponse.LoginResponse
import com.example.famchat.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val _checkDeepLink =  MutableLiveData<Pair<Boolean, CheckDeepLinkResponse?>>()
    val checkDeepLink: LiveData<Pair<Boolean, CheckDeepLinkResponse?>> get() = _checkDeepLink

    /**
     * Login with account
     *
     * @param userName: account information
     * @param password: account information
     * @return:         login result (success,fail)
     * */
    fun login(
        userName: String,
        password: String,
        showDialogResourceSuccess:(BaseResponse<LoginResponse>) -> Boolean = { false },
        result: (BaseResponse<LoginResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.login(userName, password)
            resourceWithErrorDefault(
                resource, ApiErrorAction.NONE,
                showDialogResourceSuccess = {
                    showDialogResourceSuccess.invoke(it.data)
                }
            ) {
                result.invoke(it.data)
            }
        }
    }

}