package com.example.famchat.model.reponse

import com.example.famchat.model.database.UserLogin
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("custType")
    var customerType: Int? = -1,
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("tin")
    var tin: String? = "",
    @SerializedName("idNo")
    var idNo: String? = "",
    @SerializedName("avatar")
    var avatar: String? = ""
) : Serializable