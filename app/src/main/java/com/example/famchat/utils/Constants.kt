package com.example.famchat.utils

import android.os.Build
import android.telephony.TelephonyManager.TimeoutException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException

const val REMAIN_DURATION = "REMAIN_DURATION"
const val CA = "CA"
const val TYPE_VAS_PACKAGE = "VP"
const val CONNECT_SME_CA = "CONNECT_SME_CA"
const val CONNECT_SME = "CONNECT_SME"
const val BBBGSME = "BBBGSME"
const val PDF_TYPE = ".pdf"
const val ERROR_VAS_USETIME_MAIN_PACKAGE = "ERROR_VAS_USETIME_MAIN_PACKAGE"
const val MYSIGN = "MYSIGN"
const val CUSTOMER_ACTIVE_KEY_NOT_EXPIRE = "CUSTOMER_ACTIVE_KEY_NOT_EXPIRE"
const val CUSTOMER_ACTIVE_NOT_KEY_EXPIRE = "CUSTOMER_ACTIVE_NOT_KEY_EXPIRE"
const val COMPLETE_ORDER_REGISTER_NEW = "/complete_order_register_new"
const val SECRET_KEY = "secretKey"
const val APPROVE_HANDOVER = "APPROVE_HANDOVER"
const val AI_CHECK = "AI_CHECK"
const val RESPONSE_STATUS_6000 = "6000"
const val MySign = "MySign"
const val MONTH_9002 = "9002"
const val DAY_9003 = "9003"
const val ON_1 = "1"
const val ID_NO = "idNo"
const val AGENCY = "agency"
const val CALL_BACK = "callBack"
const val NAME = "name"
const val PARAM = "param"
const val OPEN_SCREEN = "/open_screen"
const val VIE_EXTEND = "giahan"
const val ON = "ON"
const val LANG_EN = "en"
const val RESTORE_USER_NOTFOUND = "restore.user.notfound"
const val RESTORE_FAIL = "restore.fail"
const val PAYMENT_TIMEOUT_ERROR = "PAYMENT_TIMEOUT_ERROR"
const val PAYMENT_TIMEOUT_ERROR_LOWERCASE = "payment.timeout.error"
const val PRODUCT_CODE_NOT_RENEW_SUB = "PRODUCT_CODE_NOT_RENEW_SUB"
const val PRODUCT_CODE_NOT_CHANGE_PRODUCT = "PRODUCT_CODE_NOT_CHANGE_PRODUCT"
const val PRODUCT_CODE_NOT_BUY_VAS = "PRODUCT_CODE_NOT_BUY_VAS"
const val OPEN_FROM_SCREEN = "OPEN_FROM_SCREEN"
const val REQUEST_SIGN = "request_sign"
const val HOST_APPLINK = "remotesigning.viettel.vn"
const val OUTSIDE_MYSIGN = "OUTSIDE_MYSIGN"
const val ADD_POINT_ERROR = "ADD_POINT_ERROR"
const val APPLINK_VNEID_PATH = "/mysignws/vneid/regAgency"
const val APPLINK_VNEID_AGENCY = "agency"
const val APPLINK_VNEID_IDNO = "idNo"
const val APPLINK_VNEID_MAIN_CODE = "mainCode"
const val APPLINK_VNEID_VAS_CODE = "vasCode"
const val PHOTO_BACK_INVALID = "PHOTO_BACK_INVALID"
const val AS_CA_REGISTER_CANCEL_VAS = "AS_CA_REGISTER_CANCEL_VAS"
const val ERROR_CODE_059 = "059"
const val ERROR_CODE_055 = "055"
const val ERROR_CODE_054 = "054"
const val ERROR_CODE_048 = "048"
const val BATCH_SPECIAL = "BATCH_SPECIAL"
const val BATCH_OFFLINE_SPECIAL = "BATCH_OFFLINE_SPECIAL"
const val ENABLE_LONG_1 = 1L
const val MS_VNEID = "MS_VNEID"

enum class CustomerType(val type: String) {
    IndividualCustomer("1"),
}

val listTimeout = mutableListOf<String>(
    java.util.concurrent.TimeoutException::class.java.name,
    TimeoutCancellationException::class.java.name,
    SocketTimeoutException::class.java.name,
).apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) add(TimeoutException::class.java.name)
}

enum class TypeRegisteredDevice(val value: Int) {
    DeviceMySign(0),
    DeviceOther(1),
}
