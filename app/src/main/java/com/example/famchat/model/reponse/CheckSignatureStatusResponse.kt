package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName

data class CheckSignatureStatusResponse(
    @SerializedName("signatures")
    var signatures: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("description")
    var description: String?
)