package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName

data class CheckDeepLinkResponse(
    @SerializedName("isDeeplink")
    var isDeeplink: Boolean = false,
    @SerializedName("includeVAS")
    var includeVAS: Boolean = false,
)
