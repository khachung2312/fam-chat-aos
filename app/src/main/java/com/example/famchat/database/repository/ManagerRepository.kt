package com.example.famchat.database.repository

import com.example.famchat.model.database.UserLogin

interface ManagerRepository {
    suspend fun keepDataLogin(userLogin: UserLogin)
    suspend fun clearDataLogin()
    suspend fun getUserLastLogin(): UserLogin?

}