package com.example.famchat.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChatRequest(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("threadId")
    var threadId: String? = null
)
