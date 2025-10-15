package com.example.famchat.activity.main.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.famchat.config.CallType
import com.example.famchat.model.reponse.LoginResponse

data class Contact(
    val name: String,
    val status: String = "Offline",
    val todayStatus: String = "",
    val avatarRes: Int,
    val lastCallTime: String = "",
    val callType: CallType = CallType.INCOMING
)

