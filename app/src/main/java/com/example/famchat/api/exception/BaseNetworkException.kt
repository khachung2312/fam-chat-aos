package com.example.famchat.api.exception

class BaseNetworkException(
    val responseMessage: String? = null,
    val responseCode: Int = -1,
    val exceptionName: String = "",
) : Exception()