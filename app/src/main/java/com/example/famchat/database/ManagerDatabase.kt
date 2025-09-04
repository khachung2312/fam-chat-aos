package com.example.famchat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.famchat.model.database.UserLogin


@Database(
    entities = [UserLogin::class],
    version = 3,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class ManagerDatabase : RoomDatabase() {
    abstract fun launcherDao(): ManagerModelDAO

    companion object {
        private const val DB_NAME = "famChat"

        fun getInstance(context: Context): ManagerDatabase {
            return Room
                .databaseBuilder(context, ManagerDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_device ADD device_setting varchar(255)")
                database.execSQL("ALTER TABLE user_device ADD device_certificate varchar(255)")
            }
        }
    }


}