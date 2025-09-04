package com.example.famchat.viewmodel.globall

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.famchat.database.repository.ManagerRepositoryImpl
import com.example.famchat.model.reponse.CheckDeepLinkResponse
import com.example.famchat.model.reponse.LoginResponse
import com.example.famchat.model.reponse.UserResponse
import org.koin.java.KoinJavaComponent.inject

class GlobalViewModel(application: Application) : AndroidViewModel(application) {

    val database: ManagerRepositoryImpl by inject<ManagerRepositoryImpl>(ManagerRepositoryImpl::class.java)

    /** User information */
    var userInformation = MutableLiveData<UserResponse>()

    var userLogin: LoginResponse? = null

    var currentScreenName: String = ""

    fun cleanData() {
        userInformation.postValue(UserResponse())
    }
}