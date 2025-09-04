package com.example.famchat.model.reponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ConfigDataResponse(

    @SerializedName("MYSIGN_MAINTENANCE_NOTIFICATION_ENG")
    var maintenanceNotificationEng: MaintenanceConfig? = null,
    @SerializedName("MYSIGN_MAINTENANCE_NOTIFICATION")
    var maintenanceNotification: MaintenanceConfig? = null,
    @SerializedName("MYSIGN_MAINTENANCE_WARNING_ENG")
    var maintenanceWarningEng: MaintenanceConfig? = null,
    @SerializedName("MYSIGN_MAINTENANCE_WARNING")
    var maintenanceWarning: MaintenanceConfig? = null,
    @SerializedName("VERSION_ANDROID")
    var appVersion: AppVersion? = null,
    @SerializedName("NEW_FEATURE_INFO_VN")
    var newFeatureInfoVn: NewFeatureInfo? = null,
    @SerializedName("NEW_FEATURE_INFO_EN")
    var newFeatureInfoEn: NewFeatureInfo? = null,
    @SerializedName("SESSION_TIME_INACTIVE")
    var timeOutSession: TimeOutSession? = null,
    @SerializedName("CODE_LIMIT_DEVICE_REGISTER")
    var limitDeviceRegister: LimitDeviceRegister? = null,
    @SerializedName("CODE_ON_OFF_SIGN_UPLOAD_FILE")
    var onOffFeatureUploadFile: OnOffData? = null,
    @SerializedName("CODE_ON_OFF_RECHARGE_OCS")
    var onOffFeatureRechargeOcs: OnOffData? = null,
    @SerializedName("MYSIGN_ND13_ONOFF")
    var onOffAcceptPolicyNd13: RequireOnOffConfig?,
    @SerializedName("MYSIGN_ND13_DESCRIPTION")
    var nd13DescVn: RequireOnOffConfig?,
    @SerializedName("MYSIGN_ND13_DESCRIPTION_EN")
    var nd13DescEn: RequireOnOffConfig?,
    @SerializedName("MYSIGN_ND13_DETAIL")
    var nd13DetailVn: DetailNd13?,
    @SerializedName("MYSIGN_ND13_DETAIL_EN")
    var nd13DetailEn: DetailNd13?,
    @SerializedName("ON_OFF_UPLOAD_FILE_NEW")
    var onOffUploadFileNew: OnOffData? = null,
    @SerializedName("RECOMMEND_CHANGE_PW_DAYS")
    var recommendChangePwDay: RecommendChangePwDay? = null,
    @SerializedName("Y_DAY_BEFORE_EXPIRED_CTS")
    var recommendExpiredCtsDay: RecommendChangePwDay? = null,
    @SerializedName("TIME_AUTO_CLOSE_POPUP")
    var timeAutoClosePopup: TimeAutoClosePopup? = null,
    @SerializedName("APP_RESOURCES")
    var appResource: AppResource? = null,
    @SerializedName("ON_OFF_CHANGE_PHONE")
    var onOffChangePhone: OnOffData? = null,
    @SerializedName("ON_OFF_HUB")
    var onOffHubWebView: OnOffData? = null,
    @SerializedName("CODE_TIME_CLOSE_TOAST")
    var timeAutoCloseToast: TimeAutoCloseToast? = null,
    @SerializedName("CONFIG_EKYC_REQUIRED_FACE")
    var configEkycRequiredFace: ConfigEkycRequiredFace? = null,
    @SerializedName("ON_OFF_HOME_FORCE_UPDATE")
    var onOffHomeForceUpdate: OnOffData,
    @SerializedName("ALLOW_UPLOAD_ID_IMAGE")
    var onOffUploadIdFromGallery: OnOffData,
    @SerializedName("AI_UPLOAD_MAX_SIZE")
    var aiSizeUpload: SizeConfig,
    @SerializedName("ON_OFF_MYBOX")
    var onOffMyBox: OnOffData,
    @SerializedName("ALLOW_CHANGE_SUB_INFO")
    var onOffChangeInfo: OnOffData,
    @SerializedName("ON_OFF_EXTEND_VAS_FUNCTION")
    var onOffExtendVasFunction: OnOffData,
    @SerializedName("ALLOW_REGISTER_NORMAL")
    var onOffRegisterNormal: OnOffData,
    @SerializedName("ALLOW_REGISTER_OVER_PROFILE")
    var onOffRegisterOverProfile: OnOffData,
    @SerializedName("ENABLE_DELETE_USER")
    var enableDeleteUser: OnOffData,
    @SerializedName("ON_OFF_CAMPAIGN_PROMOTION_VTPLUS")
    var enablePromotion: OnOffData,
    @SerializedName("ENABLE_SKIP_EKYC_FOR_VNEID")
    var onOffSkipEkycVneid: OnOffData,
    @SerializedName("ENABLE_SURVEY")
    var enableSurvey: OnOffData,
    @SerializedName("GUIDE_IN_REGISTER")
    var guideRegister: GuideRegister,
    @SerializedName("DEEPLINK_VNEID")
    var deeplinkVNeID: Deeplink,
    @SerializedName("ON_OFF_CARD_CTS_HOMESCREEN")
    var onOffCtsHome: OnOffData,
    @SerializedName("ON_OFF_REGISTER_VIA_VNEID")
    var onOffRegisterVNeID: OnOffData,
    @SerializedName("LIST_VAS_SELECTED")
    var listVasDefault: ListVasSelected,
) : Serializable

open class ConfigEkycRequiredFace : Serializable {
    @SerializedName("EKYC_LEFT")
    var ekycLeft: Int = 1

    @SerializedName("EKYC_RIGHT")
    var ekycRight: Int = 1

    @SerializedName("EKYC_BLINK")
    var ekycBlink: Int = 1

    @SerializedName("EKYC_SMILE")
    var ekycSmile: Int = 1
}

open class OnOffData : Serializable {
    @SerializedName("onOff")
    var onOff: String? = null
}

open class MaintenanceConfig : OnOffData() {
    @SerializedName("message")
    var message: String? = null
}

class RequireOnOffConfig : OnOffData() {
    @SerializedName("require")
    var require: String? = null

    @SerializedName("message")
    var message: String? = null
}

class PolicyConfig : OnOffData() {
    @SerializedName("code")
    var code: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("selected")
    var selected: Boolean? = null

    @SerializedName("enable")
    var enable: Boolean? = true

    @SerializedName("subtitle")
    var subtitle: String? = null

    var ismandatory = false
    var isSelected = false
}

class AppVersion : Serializable {
    @SerializedName("link")
    var link: String? = null

    @SerializedName("forceUpdate")
    var forceUpdate: String? = null

    @SerializedName("version")
    var version: String? = null
}

class NewFeatureInfo : Serializable {
    @SerializedName("content")
    var content: String? = null
}

class TimeOutSession : Serializable {
    @SerializedName("timeout")
    var timeout: String? = null
}

class LimitDeviceRegister : Serializable {
    @SerializedName("limit")
    var limit: String? = null
}

class DetailNd13 : Serializable {

    @SerializedName("mandatory")
    var mandatory: Array<PolicyConfig>? = null

    @SerializedName("options")
    var options: Array<PolicyConfig>? = null

    @SerializedName("confirmInfo")
    var confirmInfo: ConfirmInfo? = null
}

class RecommendChangePwDay : Serializable {
    @SerializedName("days")
    var days: String? = null

}

class TimeAutoClosePopup : Serializable {
    @SerializedName("autoClosePopup")
    var autoClosePopup: String? = null
}

class TimeAutoCloseToast : Serializable {
    @SerializedName("seconds")
    var seconds: String? = null
}

class AppResource : Serializable {
    @SerializedName("splashScene")
    var bgSplash: String? = null

    @SerializedName("loginBg")
    var bgLogin: String? = null
}

class SizeConfig : Serializable {
    @SerializedName("size")
    var size: String? = null
}

data class Deeplink(
    @SerializedName("link")
    var link: String? = null
)

data class GuideRegister(
    @SerializedName("vi")
    var vi: List<String> = listOf(),
    @SerializedName("en")
    var en: List<String> = listOf(),
)

class ConfirmInfo: Serializable {
    @SerializedName("link")
    var link: String? = null
}

data class ListVasSelected(
    @SerializedName("data")
    val data: List<PackageMainConfig>,
)

data class PackageMainConfig(
    @SerializedName("mainCode")
    val mainCode: String,
    @SerializedName("selectDefault")
    val selectDefault: List<String>
)

