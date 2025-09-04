package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterAccountResponse(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("fullName")
    var fullName: String? = null,
) : Serializable