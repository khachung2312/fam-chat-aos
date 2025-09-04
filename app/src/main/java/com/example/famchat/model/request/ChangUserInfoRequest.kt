package com.example.famchat.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChangUserInfoRequest(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("userLanguage")
    var userLanguage: String,
)
