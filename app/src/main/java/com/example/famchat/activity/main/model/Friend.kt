package com.example.famchat.activity.main.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.famchat.config.CallType
import com.example.famchat.model.reponse.LoginResponse

data class Friend(
    val name: String,
    val avatarRes: Int,
    val hasUnseenStory: Boolean = false
)


