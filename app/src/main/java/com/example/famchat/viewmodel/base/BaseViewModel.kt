package com.example.famchat.viewmodel.base

import android.app.Application
import android.text.TextUtils
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.famchat.FamChatManager
import com.example.famchat.R
import com.example.famchat.api.ApiHelper
import com.example.famchat.api.Resource
import com.example.famchat.config.ApiErrorAction
import com.example.famchat.config.Constants
import com.example.famchat.database.repository.ManagerRepositoryImpl
import com.example.famchat.dialog.showDialogNotify
import com.example.famchat.extensions.showToastError
import com.example.famchat.extensions.showToastSuccess
import com.example.famchat.model.database.UserLogin
import com.example.famchat.model.reponse.BaseResponse
import com.example.famchat.utils.NetworkUtils
import com.example.famchat.utils.listTimeout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val database: ManagerRepositoryImpl by inject<ManagerRepositoryImpl>(ManagerRepositoryImpl::class.java)
    var isLoading = MutableLiveData<Boolean>(null)
    var apiHelper = ApiHelper()


    fun clearDataLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearDataLogin()
        }
    }

    fun keepDataLogin(userLogin: UserLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            database.keepDataLogin(userLogin)
        }
    }


    suspend fun getUserLastLogin(): UserLogin? {
        return database.getUserLastLogin()
    }


    /**
     *  Handle error to show message
     *  @param message: content toast
     * */
    fun showErrorDefault(
        message: String? = null,
        apiErrorAction: ApiErrorAction = ApiErrorAction.SHOW_TOAST
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentActivity = FamChatManager.instance().currentActivity ?: return@launch
            if (apiErrorAction == ApiErrorAction.SHOW_POPUP) {

                var icon = R.drawable.fc_ic_error
                currentActivity.showDialogNotify(
                    icon = icon,
                    message = if (TextUtils.isEmpty(message)) {
                        currentActivity.getString(R.string.fc_app_name)
                    } else {
                        message ?: ""
                    },
                    positiveText = R.string.fc_close
                )
            } else if (apiErrorAction == ApiErrorAction.SHOW_TOAST) {
                val rootView =
                    (currentActivity.findViewById(android.R.id.content) as? ViewGroup)?.getChildAt(0)
                rootView?.showToastError(
                    message.toString(),
                    R.drawable.fc_ic_sercutity,
                    android.widget.Toast.LENGTH_LONG
                )
            }
        }
    }

    /**
     *  Handle error to show message
     *  @param message: content toast
     * */
    fun showSuccessDefault(
        message: String? = null,
        apiErrorAction: ApiErrorAction = ApiErrorAction.SHOW_TOAST
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentActivity = FamChatManager.instance().currentActivity ?: return@launch
            if (apiErrorAction == ApiErrorAction.SHOW_POPUP) {

                var icon = R.drawable.fc_ic_success

                currentActivity.showDialogNotify(
                    icon = icon,
                    message = if (TextUtils.isEmpty(message)) {
                        currentActivity.getString(R.string.fc_app_name)
                    } else {
                        message ?: ""
                    },
                    positiveText = R.string.fc_close
                )
            } else if (apiErrorAction == ApiErrorAction.SHOW_TOAST) {
                val rootView =
                    (currentActivity.findViewById(android.R.id.content) as? ViewGroup)?.getChildAt(0)
                rootView?.showToastSuccess(
                    message.toString(),
                    R.drawable.fc_ic_circle_check,
                    android.widget.Toast.LENGTH_LONG
                )
            }
        }
    }

    /**
     *  Handle response from api. Allow show message error default
     *  @param resource:                result from api
     *  @param apiErrorAction:   = action show message error from api (response code == 200 && !data.success)
     *  @return:                        Data if success (response code == 200 && data.success)
     * */
    fun <T : Any> resourceWithErrorDefault(
        resource: Resource<T>,
        apiErrorAction: ApiErrorAction = ApiErrorAction.SHOW_TOAST,
        closeViewLoading: Boolean = true,
        resourceError: (Resource.Error) -> Boolean = { false },
        showDialogResourceSuccess: (Resource.Success<T>) -> Boolean = { false },
        resourceSuccess: (Resource.Success<T>) -> Unit,
    ) {
        if (closeViewLoading) {
            isLoading.postValue(false)
        }
        when (resource) {
            is Resource.Success -> {
                if (!showDialogResourceSuccess.invoke(resource)) {
                    if (resource.data is BaseResponse<*> && !resource.data.success) {
                        showErrorDefault(
                            message = resource.data.message,
                            apiErrorAction = apiErrorAction
                        )
                    } else if (resource.data is BaseResponse<*> && resource.data.success) {
                        showSuccessDefault(
                            message = resource.data.message,
                            apiErrorAction = apiErrorAction
                        )
                    }
                }
                resourceSuccess.invoke(resource)
            }

            is Resource.Error -> {
                FamChatManager.instance().currentActivity?.let {
                    if (!NetworkUtils.isNetworkConnected(it)) {
                        it.showDialogNotify(
                            title = R.string.fc_app_name,
                            message = it.getString(R.string.fc_app_name),
                            positiveText = R.string.fc_app_name
                        )
                        return
                    }
                }
                if (resource.exception.responseCode == Constants.ErrorCode.TOKEN_MY_SIGN_INVALID) {
                    FamChatManager.instance().currentActivity?.logout()
                    return
                }
                when (resource.exception.responseMessage) {
                    Constants.MSErrorCode.SESSION_EXPIRED -> {
                        FamChatManager.instance().currentActivity?.let {
                            showErrorDefault(
                                message = it.getString(R.string.fc_app_name),
                                apiErrorAction = ApiErrorAction.SHOW_TOAST
                            )
                            it.logout()
                        }
                    }

                    Constants.MSErrorCode.DEVICE_OTHER_LOGIN -> {
                        FamChatManager.instance().currentActivity?.let { activity ->
                            activity.showDialogNotify(
                                message = activity.getString(R.string.fc_app_name),
                                icon = R.drawable.fc_ic_error,
                                positiveText = R.string.fc_app_name,
                                dismissListener = {
                                    activity.logout()
                                }
                            )
                        }
                    }

                    else -> {
                        if (resource.exception.responseCode == Constants.ErrorCode.TOKEN_MY_SIGN_INVALID) {
                            FamChatManager.instance().currentActivity?.logout()
                        } else if (apiErrorAction != ApiErrorAction.NONE) {
                            if (resourceError.invoke(resource)) return
                            FamChatManager.instance().currentActivity?.let {
                                val isHandleError =
                                    listTimeout.contains(resource.exception.exceptionName)
                                if (isHandleError) {
                                    it.showDialogNotify(
                                        message = it.getString(R.string.fc_app_name),
                                        icon = R.drawable.fc_ic_error,
                                        positiveText = R.string.fc_app_name,
                                    )
                                    return
                                }
                            }
                            showErrorDefault(
                                message = resource.exception.responseMessage,
                                apiErrorAction = apiErrorAction
                            )
                        }
                    }
                }
            }
        }
    }

}