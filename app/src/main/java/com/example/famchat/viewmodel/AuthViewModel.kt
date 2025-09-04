package com.example.famchat.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.famchat.config.ApiErrorAction
import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.model.reponse.LoginResponse
import com.example.famchat.model.reponse.RegisterAccountResponse
import com.example.famchat.model.request.ChangUserInfoRequest
import com.example.famchat.viewmodel.base.BaseViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Login with account
     *
     * @param email: account information
     * @param password: account information
     * @return:         login result (success,fail)
     * */
    fun login(
        email: String,
        password: String? = "",
        signInType: String? = "",
        isSavedDataLogin: Boolean = false,
        showDialogResourceSuccess: (BaseResponse<LoginResponse>) -> Boolean = { false },
        result: (BaseResponse<LoginResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.login(email, password, signInType)
            resourceWithErrorDefault(
                resource, ApiErrorAction.NONE,
                showDialogResourceSuccess = {
                    showDialogResourceSuccess.invoke(it.data)
                }
            ) {
                if(it.data.success && isSavedDataLogin) {
                    keepDataLogin(it.data.result!!.convertUserLoginModel())
                }
                result.invoke(it.data)
            }
        }
    }

    /**
     * register with account
     *
     * @param email: account information
     * @param phoneNumber: phoneNumber information
     * @param phoneCountryCode: phoneCountryCode information
     * @param avatar: avatar information
     * @param password: password information
     * @param fullName: fullName information
     * @param googleAccount: googleAccount information
     * @param language: language information
     * @return:         register result (success,fail)
     * */
    fun registerAccount(
        email: String,
        phoneNumber: String? = "",
        phoneCountryCode: String? = "",
        avatar: String? = "",
        password: String? = "",
        fullName: String? = "",
        googleAccount: String? = "",
        language: String? = "",
        result: (BaseResponse<RegisterAccountResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.registerAccount(
                email = email,
                phoneNumber = phoneNumber,
                phoneCountryCode = phoneCountryCode,
                avatar = avatar,
                password = password,
                fullName = fullName,
                googleAccount = googleAccount,
                language = language
            )
            resourceWithErrorDefault(resource, ApiErrorAction.SHOW_TOAST) {
                result.invoke(it.data)
            }
        }
    }

    /**
     * resend otp auth
     *
     * @param email: email receive otp
     * @param phoneNumber: phoneNumber information
     * @param forceResend: true or false to ...
     * @return:          result (success,fail)
     * */
    fun resendOTP(
        email: String,
        phoneNumber: String? = "",
        forceResend: Boolean? = false,
        result: (BaseResponse<LoginResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.resendOTP(email, phoneNumber, forceResend)
            resourceWithErrorDefault(resource, ApiErrorAction.SHOW_TOAST) {
                result.invoke(it.data)
            }
        }
    }

    /**
     * verify otp auth
     *
     * @param email: email auth
     * @param phoneNumber: phoneNumber information
     * @param otp: otp auth
     * @return:         verify result (success,fail)
     * */
    fun verifyOTP(
        email: String,
        phoneNumber: String? = "",
        otp: String,
        result: (BaseResponse<LoginResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.verifyOTP(email, phoneNumber, otp)
            resourceWithErrorDefault(resource, ApiErrorAction.NONE) {
                result.invoke(it.data)
            }
        }
    }


    /**
     * forgot password with email
     *
     * @param email: email auth
     * @param password: new password
     * @param otp: otp auth
     * @return:          result (success,fail)
     * */
    fun forgotPassword(
        email: String,
        password: String? = "",
        otp: String,
        result: (BaseResponse<LoginResponse>) -> Unit
    ) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val resource = apiHelper.forgotPassword(email, password, otp)
            resourceWithErrorDefault(resource, ApiErrorAction.SHOW_TOAST) {
                result.invoke(it.data)
            }
        }
    }




}