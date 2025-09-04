package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName

data class RegisterDeviceResponse(
    @SerializedName("alias")
    var alias: String = "",
    @SerializedName("certificate")
    var certificate: String = ""
)
