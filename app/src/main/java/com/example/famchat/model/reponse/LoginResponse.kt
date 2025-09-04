package com.example.famchat.model.reponse

import com.example.famchat.model.database.UserLogin
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LoginResponse(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("fullName")
    var fullName: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("avatar")
    var avatar: String? = null,
    @SerializedName("phoneCountryCode")
    var phoneCountryCode: String? = null,
    @SerializedName("language")
    var language: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("isEmailVerified")
    var isEmailVerified: Boolean = false,
    @SerializedName("isPhoneNumberVerified")
    var isPhoneNumberVerified: Boolean = false,
) : Serializable {
    fun convertUserLoginModel(): UserLogin {
        return UserLogin(
            id = id,
            email = email,
            phoneNumber = phoneNumber,
            fullName = fullName,
            gender = gender,
            avatar = avatar,
            phoneCountryCode = phoneCountryCode,
            language = language,
            country = country,
            isEmailVerified = isEmailVerified,
            isPhoneNumberVerified= isPhoneNumberVerified
        )
    }
}