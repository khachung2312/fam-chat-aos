package com.example.famchat.model.request

import com.google.gson.annotations.SerializedName

data class CheckSignatureStatusRequest(
    @SerializedName("transactionId")
    var transactionId: String? = ""
)