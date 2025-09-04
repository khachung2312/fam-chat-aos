package com.example.famchat.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginRequest(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("fullName")
    var fullName: String? = null,
    @SerializedName("signInType")
    var signInType: String? = null,
    @SerializedName("otp")
    var otp: String? = null,
)

