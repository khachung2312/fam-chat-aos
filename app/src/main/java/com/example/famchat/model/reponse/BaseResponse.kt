package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("code")
    var code: String = "",
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("data")
    var result: T? = null
)
