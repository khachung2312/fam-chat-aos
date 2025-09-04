package com.example.famchat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.famchat.config.ApiErrorAction
import com.example.famchat.model.reponse.UserResponse
import com.example.famchat.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

open class MainViewModel(application: Application) : BaseViewModel(application) {

    val lstAccountShowBalance = mutableListOf<String>()
    var countNotifyUnRead = MutableLiveData(0)

    private val _streamedText = MutableStateFlow("")
    val streamedText: StateFlow<String> = _streamedText

    /**
     * Get user information
     * @return: user information
     * */


    fun sendChatMessage(
        message: String,
    ) {
        val chatRequest = JSONObject().apply {
            put("message", message)
            put("threadId", "3")
        }.toString()

        isLoading.postValue(true)
        apiHelper.sendChatMessage(
            jsonBody = chatRequest,
            onEachChunk = { chunk ->
                // Cập nhật UI phải chạy trên Main thread
                viewModelScope.launch(Dispatchers.Main) {
//                    Log.d("--------------", chunk)
                    _streamedText.emit(chunk)
                    isLoading.value = false
                }
            },
            onSuccess = {
                viewModelScope.launch(Dispatchers.Main) {
                    isLoading.value = false
                }
            },
            onError = { throwable ->
                viewModelScope.launch(Dispatchers.Main) {
                    isLoading.value = false
                    Log.e("ChatStream", "Error", throwable)
                }
            }
        )
    }


}