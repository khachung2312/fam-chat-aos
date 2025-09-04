package com.example.famchat.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegisterAccountRequest(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("fullName")
    var fullName: String? = null,
    @SerializedName("language")
    var language: String? = null,
    @SerializedName("googleAccount")
    var googleAccount: String? = null,
    @SerializedName("phoneCountryCode")
    var phoneCountryCode: String? = null,
    @SerializedName("avatar")
    var avatar: String? = null,
)
