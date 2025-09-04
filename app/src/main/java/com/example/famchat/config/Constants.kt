package com.example.famchat.config

import android.net.Uri

object Constants {

    fun uriDeepLink() = Uri.Builder().apply {
        scheme("mysign")
        authority("mysignws")
    }

    /**
     * Value default global
     * */
    object DefaultValue {
        const val ACCOUNT_REVIEW = "duynq7_viettel2"
        const val LANGUAGE_DEFAULT = "vi"
        const val TIME_OTP_EXPIRE = 60
    }

    /**
     * Key of sharedPreferences
     * */
    object Preferences {
        const val VERSION_INSTALL = "version_install"
        const val KEY_LANGUAGE = "key_language"
        const val KEY_THEMES = "key_themes"
        const val SHOW_SUGGEST_ACCOUNT = "show_suggest_account"
        const val ACCOUNT_LAST_LOGIN = "account_last_login"
        const val FIREBASE_TOKEN = "firebase token message"

        const val BG_SPLASH = "background splash"
        const val SHOW_ONBOARD_PRE_LOGIN = "SHOW_ONBOARD_PRE_LOGIN"
        const val SHOW_ONBOARD_REGISTER = "SHOW_ONBOARD_REGISTER"
        const val SHOW_ONBOARD_HOME = "SHOW_ONBOARD_HOME"
        const val CALL_DEEPLINK_DEVICE_ID = "CALL_DEEPLINK_DEVICE_ID"
        const val USERS_NAME_LOGGED = "USERS_NAME_LOGGED"

    }

    /**
     * Key of themes
     * */
    object THEMES {
        const val THEME_SYSTEM = "system"
        const val THEME_DARK = "dark"
        const val THEME_LIGHT = "light"
        const val THEME_KEY = "theme_mode"
    }

    /**
     * host deeplink and notification
     * */
    object HostDeepLink {

        /** Điều hướng màn hình theo tên, từ deeplink */
        const val SCREEN_NAVIGATION = "open_screen"

        /** Chức năng hoàn thiện đơn hàng*/
        const val COMPLETE_ORDER_REGISTER = "complete_order_register"

    }

    /**
     * screen name, use with deeplink open_screen
     * */
    object ScreenName {
        /**
         *  KHÔNG CẦN LOGIN
         *  */
        const val SCREEN_LOGIN = "login"
        const val SCREEN_REGISTER_ACCOUNT = "register_account"
        const val SEARCH_ORDER = "timkiem_donhang"

        /**
         * BẮT BUỘC LOGIN
         * */
        const val LIST_REQUEST_SIGN = "danhsach_xacthucky"
        const val BUY_PACKAGE = "mua_goicuoc"
        const val REVOKE_CTS = "thuhoi_chungthuso"
        const val EXTENSION_CTS = "giahan_chungthuso"
        const val CHANGE_PACKAGE = "doi_goicuoc"
        const val CREATE_SIGNATURE = "thietlap_mau_chuky"
        const val SYSTEM_LOCKUP = "hethong_tichhop"
        const val NOTIFICATION = "thongbao"
        const val ACCOUNT_INFO = "thongtin_taikhoan"
        const val REPORT = "baocao"
        const val ACCEPTANCE = "nghiemthu_dichvu"

        const val CHANGE_PHONE = "doi_sodientho"
        const val LIST_CTS = "danhsach_cts"
        const val MANAGE_DEVICE = "quanly_thietbi"
        const val CREATE_REQUEST = "tao_yeucau_ky"
        const val REGISTER_DEVICE = "dangky_thietbi"
        const val SETTING = "caidat"
        const val CHANGE_PASSWORD = "doimatkhau"
        const val APP_INFO = "thongtin_phienban"
        const val INVITE_FRIEND = "moi_banbe"
        const val ORDER = "order"
        const val USER_INFO = "user_info"
        const val CHAT = "chat"

    }

    /**
     * Key sharedPreferences save finger
     * */
    object Finger {
        const val FINGER_PRINT = "finger_print"
        const val FINGER_PRINT_KEY_IV = "finger_print_key_iv"
        const val FINGER_PRINT_KEY = "finger_print_key"
    }

    /**
     * Error Code Api
     * */
    object ErrorCode {
        const val FORCE_LOGOUT = -999
        const val TOKEN_MY_SIGN_INVALID = 403
    }

    /**
     * Error Code Api
     * */
    object MSErrorCode {
        const val SUCCESS = "success"
        const val SESSION_EXPIRED = "session.expired"
        const val DEVICE_OTHER_LOGIN = "device.logined.other.device"
        const val DEVICE_LIMIT = "login.limited.device"
    }

    object LanguageCode {
        const val enUsCode = "enUS"
        const val enGbCode = "enGB"
        const val zhCnCode = "zhCN"
        const val zhTwCode = "zhTW"
        const val hiInCode = "hiIN"
        const val esEsCode = "esES"
        const val esMxCode = "esMX"
        const val frFrCode = "frFR"
        const val frCaCode = "frCA"
        const val arSaCode = "arSA"
        const val bnBdCode = "bnBD"
        const val ptPtCode = "ptPT"
        const val ptBrCode = "ptBR"
        const val ruRuCode = "ruRU"
        const val urPkCode = "urPK"
        const val deDeCode = "deDE"
        const val jaJpCode = "jaJP"
        const val koKrCode = "koKR"
        const val itItCode = "itIT"
        const val trTrCode = "trTR"
        const val viVnCode = "viVN"
        const val thThCode = "thTH"
        const val faIrCode = "faIR"
        const val plPlCode = "plPL"
        const val nlNlCode = "nlNL"
        const val elGrCode = "elGR"
        const val swKeCode = "swKE"
        const val haNgCode = "haNG"
        const val yoNgCode = "yoNG"
        const val zuZaCode = "zuZA"
        const val amEtCode = "amET"
        const val soSoCode = "soSO"
    }

    object CountryCode {

    }
}