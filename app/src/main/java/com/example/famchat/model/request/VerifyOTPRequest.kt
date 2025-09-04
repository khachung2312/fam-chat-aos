package com.example.famchat.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifyOTPRequest(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("otp")
    var otp: String,
)
