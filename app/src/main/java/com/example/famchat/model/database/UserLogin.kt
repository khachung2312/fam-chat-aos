package com.example.famchat.model.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.famchat.model.reponse.LoginResponse

@Keep
@Entity(tableName = "user_login")
data class UserLogin(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo("email")
    var email: String? = "",
    @ColumnInfo("phoneNumber")
    var phoneNumber: String? = "",
    @ColumnInfo("fullName")
    var fullName: String? = "",
    @ColumnInfo("gender")
    var gender: String? = "",
    @ColumnInfo("avatar")
    var avatar: String? = "",
    @ColumnInfo("phoneCountryCode")
    var phoneCountryCode: String? = "",
    @ColumnInfo("language")
    var language: String? = "",
    @ColumnInfo("country")
    var country: String? = null,
    @ColumnInfo("isEmailVerified")
    var isEmailVerified: Boolean = false,
    @ColumnInfo("isPhoneNumberVerified")
    var isPhoneNumberVerified: Boolean = false,
    @ColumnInfo("last_update")
    var lastUpdate: Long = System.currentTimeMillis()
) {
    fun convertUserLoginModel(): LoginResponse {
        return LoginResponse(
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