package com.example.famchat.model

data class UserData(
    /** account login */
    var userName: String = "",

    /** user id from login api */
    var userId: String = "",

    /** hardcode android */
    var platform: String = "android",

    /** device-id get from CA-SDK CloudCaUtils.createCertificate()
     *  Call registerDevice() => 1 id difference
     *  */
    var deviceIdCA: String = "",

    /** device-id get from class DeviceUuidFactory
     *  1 device = 1 id
     *  */
    var deviceIdMySign: String = "",

    /**
     * user login had validate cloud CA
     * */
    var hasValidateCloudCA : Boolean = false,

    /** refresh token from login */
    var refreshToken: String = "",

    /** access token from login */
    var accessTokenMySign: String = "",

    /** Token Expiration Time */
    var expiredIn: String = "",

    /** remaining time to authorize signing without biometric authentication */
    var sessionSignDuration: Long = 0,

    /** time setup authorize signing without biometric authentication */
    var sessionSignConfig: Float = 0f,

    /** Referral code of account */
    var invitationCode: String? = null,

    var alias: String = "",
    var certificate: String = ""
)
