package com.example.famchat.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import com.example.famchat.R
import java.io.ByteArrayInputStream
import java.util.regex.Matcher
import java.util.regex.Pattern


private const val HIDE_CHAR = "*"

fun String?.isNotEmptyOrNa(): Boolean {
    if (TextUtils.isEmpty(this)) {
        return false
    } else if (this?.trim()?.toLowerCase() == "n/a") {
        return false
    }
    return true
}

fun String?.hiddenEmail(): String {
    if (this == null) {
        return ""
    }
    val atSignIndex = this.indexOf("@")
    if (atSignIndex == -1)
        return this
    if (this.contains("*")) {
        return this
    }
    val stringBuilder = StringBuilder(this)
    val emailName = this.substring(0, atSignIndex)
    if (emailName.length == 1) {
        return stringBuilder.replace(0, emailName.length, HIDE_CHAR).toString()
    }
    return if (emailName.length < 4) {
        val hiddenNameEnd = HIDE_CHAR.repeat(emailName.length - 1)
        stringBuilder.replace(1, atSignIndex, hiddenNameEnd).toString()
    } else {
        stringBuilder.delete(3, atSignIndex)
        stringBuilder.insert(3, HIDE_CHAR.repeat(4))
        stringBuilder.toString()
    }
}

fun String?.hiddenPhoneNumber(sizeHide: Int = 3): String {
    if (this == null) {
        return ""
    }
    if (this.contains("*")) {
        return this
    }
    if (this.length < sizeHide)
        return this
    val stringBuilder = StringBuilder(this)
    stringBuilder.delete(length - sizeHide, length)
    stringBuilder.insert(length - sizeHide, HIDE_CHAR.repeat(sizeHide))
    return stringBuilder.toString()
}

fun String?.pwdValid(): Boolean {
    if (this == null) {
        return false
    }
    val pattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>.%]).{8,200}$"
    val matcher = Pattern.compile(pattern).matcher(this)
    return matcher.matches()
}

fun String?.isBase64(): Boolean {
    if (this == null) {
        return false
    }
    val pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$"
    val matcher = Pattern.compile(pattern).matcher(this)
    return matcher.matches()
}

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
}

fun String?.base64ToString(): String {
    return if (TextUtils.isEmpty(this)) {
        ""
    } else if (this!!.isBase64()) {
        String(Base64.decode(this.toByteArray(), 2))
    } else {
        this
    }
}

fun String?.safeParseInt(errorValue: Int = -1): Int {
    if (TextUtils.isEmpty(this)) {
        return errorValue
    }
    return try {
        this!!.toInt()
    } catch (ex: Exception) {
        errorValue
    }
}

fun String?.safeParseLong(errorValue: Long = 0L): Long {
    if (TextUtils.isEmpty(this)) {
        return errorValue
    }
    return try {
        this!!.toLong()
    } catch (ex: Exception) {
        errorValue
    }
}

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isOnlyNumber(): Boolean {
    val pattern: Pattern = Pattern.compile("\\d+")
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.removeVietnameseDiacritics(): String {
    var str = this
    str = str.replace("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ".toRegex(), "a")
    str = str.replace("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ".toRegex(), "e")
    str = str.replace("ì|í|ị|ỉ|ĩ".toRegex(), "i")
    str = str.replace("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ".toRegex(), "o")
    str = str.replace("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ".toRegex(), "u")
    str = str.replace("ỳ|ý|ỵ|ỷ|ỹ".toRegex(), "y")
    str = str.replace("đ".toRegex(), "d")
    str = str.replace("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ".toRegex(), "A")
    str = str.replace("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ".toRegex(), "E")
    str = str.replace("Ì|Í|Ị|Ỉ|Ĩ".toRegex(), "I")
    str = str.replace("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ".toRegex(), "O")
    str = str.replace("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ".toRegex(), "U")
    str = str.replace("Ỳ|Ý|Ỵ|Ỷ|Ỹ".toRegex(), "Y")
    str = str.replace("Đ".toRegex(), "D")
    return str
}

fun String.capitalizeFirstLetter(): String {
    return if (isNotEmpty()) {
        this[0].toUpperCase() + substring(1).toLowerCase()
    } else {
        this
    }
}

fun String?.addDotLastString(): String {
    if (this == null || TextUtils.isEmpty(this)) {
        return ""
    }
    val lastChar = this.last().toString().removeVietnameseDiacritics()
    if (Pattern.compile("^[a-zA-Z0-9]+$").matcher(lastChar).matches()) {
        return "$this."
    }
    return this
}

fun String?.base64ToByteArray(): ByteArray {
    if (TextUtils.isEmpty(this)) {
        return byteArrayOf()
    }
    return Base64.decode(this, Base64.DEFAULT)
}

fun String?.base64ToBitmap(): Bitmap? {
    if (TextUtils.isEmpty(this)) {
        return null
    }
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

    val inputStream = ByteArrayInputStream(decodedString)
    val exif = ExifInterface(inputStream)

    return when (exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> bitmap.rotateBitmap(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> bitmap.rotateBitmap(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> bitmap.rotateBitmap(270f)
        else -> bitmap
    }
}

fun String?.shareText(context: Context, titleShare: String? = null) {
    val i = Intent(Intent.ACTION_SEND)
    i.setType("text/plain")
    i.putExtra(Intent.EXTRA_TEXT, this)
    context.startActivity(
        Intent.createChooser(
            i,
            titleShare ?: context.getString(R.string.fc_app_name)
        )
    )
}

fun String?.jsonBeauty(): String {
    if (TextUtils.isEmpty(this)) {
        return ""
    }
    return this!!
        .replace("{", "{\n  ")
        .replace("}", "\n}")
        .replace("[", "{\n  ")
        .replace("]", "\n]")
        .replace(",", ",\n  ")
}

fun String?.stringOrPlank(): String {
    return this?.let {
        if (TextUtils.isEmpty(this) || this.trim().toLowerCase() == "n/a") {
            ""
        } else {
            this
        }
    } ?: run {
        ""
    }
}

fun String.validatePhoneNumber(run: (Boolean) -> Unit = {}): Boolean {
    val isPass =
        this.matches("^[0-9]{10,11}$".toRegex()) && (this.startsWith("84") || this.startsWith("0"))
    run.invoke(isPass)
    return isPass
}

fun String.validateEmail(run: (Boolean) -> Unit = {}): Boolean {
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    val isPass = EMAIL_ADDRESS_PATTERN.matcher(this).matches()
    run.invoke(isPass)
    return isPass
}

fun String?.ifNullOrEmpty(changeValue: () -> String = { "" }): String {
    return if (this.isNullOrEmpty()) changeValue.invoke() else this
}

fun String?.ifNotNullOrEmpty(run: (String) -> Unit = {}) {
    if (!this.isNullOrEmpty()) run.invoke(this)
}


fun String?.isLetter(): Boolean {
    if (this.isNullOrEmpty()) return false
    var isLetter = false
    this.toCharArray().forEach {
        if (it.isLetter()) isLetter = true
    }
    return isLetter
}

fun String.trimPhoneNumber(): String {
    return when {
        this.startsWith("0") -> this.substring(1)
        this.startsWith("84") -> this.substring(2)
        else -> this
    }
}

fun ignoreCaseOpt(ignoreCase: Boolean) =
    if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
    pat.toRegex(ignoreCaseOpt(ignoreCase))
        .findAll(this?: "")
        .map { it.range.first }
        .toList()