package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName

data class DeviceSettingResponse(
    @SerializedName("device_key_type")
    var deviceKeyType: String = "",
    @SerializedName("device_key_size")
    var deviceKeySize: Int,
    @SerializedName("secure_element_required")
    var secureElementRequired: Boolean = false,
    @SerializedName("biometric_required")
    var biometricRequired: Boolean = false,
    @SerializedName("allowed_devices")
    var allowedDevices: String = ""
)