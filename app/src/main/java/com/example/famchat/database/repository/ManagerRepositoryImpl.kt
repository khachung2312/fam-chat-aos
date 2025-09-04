package com.example.famchat.database.repository

import com.example.famchat.database.ManagerDatabase
import com.example.famchat.model.database.UserLogin


class ManagerRepositoryImpl(
    private val managerDatabase: ManagerDatabase
) : ManagerRepository {

    /**
     * Save data user login
     * */
    override suspend fun keepDataLogin(userLogin: UserLogin) {
        managerDatabase.launcherDao().insertUserLogin(userLogin)
    }

    override suspend fun clearDataLogin() {
        managerDatabase.launcherDao().clearUserLogin()
    }


    /**
     * get user last login
     * */
    override suspend fun getUserLastLogin(): UserLogin? {
        return managerDatabase.launcherDao().getUserLastLogin()
    }

}