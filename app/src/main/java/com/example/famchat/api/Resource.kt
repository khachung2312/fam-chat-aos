package com.example.famchat.api

import com.example.famchat.api.exception.BaseNetworkException


sealed class Resource<out T> {

    data class Success<out T : Any>(val data: T) : Resource<T>()

    data class Error(val exception: BaseNetworkException) : Resource<Nothing>()

}