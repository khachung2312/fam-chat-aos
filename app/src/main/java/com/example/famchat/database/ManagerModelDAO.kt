package com.example.famchat.database

import androidx.room.*
import com.example.famchat.model.database.UserLogin

@Dao
interface ManagerModelDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserLogin(userLogin: UserLogin)

    @Query("DELETE FROM user_login")
    fun clearUserLogin()

    @Query("SELECT * FROM user_login ORDER BY last_update DESC LIMIT 1")
    fun getUserLastLogin(): UserLogin?

}