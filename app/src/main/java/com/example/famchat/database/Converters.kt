package com.example.famchat.database

import androidx.room.TypeConverter
import com.example.famchat.model.reponse.DeviceSettingResponse
import com.example.famchat.model.reponse.RegisterDeviceResponse
import com.example.famchat.utils.SecurityUtils
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toDeviceSettingResponse(deviceSettingResponse: String?): DeviceSettingResponse? {
        return deviceSettingResponse?.let {
            Gson().fromJson(
                SecurityUtils.decrypt(deviceSettingResponse),
                DeviceSettingResponse::class.java
            )
        } ?: run {
            null
        }

    }

    @TypeConverter
    fun fromDeviceSettingResponse(deviceSettingResponse: DeviceSettingResponse): String {
        return SecurityUtils.encrypt(Gson().toJson(deviceSettingResponse))
    }

    @TypeConverter
    fun toRegisterDeviceResponse(registerDeviceResponse: String?): RegisterDeviceResponse? {
        return registerDeviceResponse?.let {
            Gson().fromJson(
                SecurityUtils.decrypt(registerDeviceResponse),
                RegisterDeviceResponse::class.java
            )
        } ?: run {
            null
        }
    }

    @TypeConverter
    fun fromRegisterDeviceResponse(registerDeviceResponse: RegisterDeviceResponse): String {
        return SecurityUtils.encrypt(Gson().toJson(registerDeviceResponse))
    }
}