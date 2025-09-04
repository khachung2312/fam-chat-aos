package com.example.famchat.extensions

import android.content.Context
import android.text.Spanned
import com.example.famchat.R
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.toIntOnlyValue(): Int {
    return (this % Int.MAX_VALUE).toInt()
}

fun Long.millisToTimeString(context: Context): String {
    val second = this / 1000
    if (second < 60) {
        return "$second s"
    }
    val minute = second / 60
    if (minute in 0..1) {
        return "$minute ${context.getString(R.string.ca_minute)}"
    } else if (minute in 2..59) {
        return "$minute ${context.getString(R.string.ca_minutes)}"
    }
    val hour = minute / 60
    if (hour in 0..1) {
        return "$hour ${context.getString(R.string.ca_hour)}"
    } else if (hour in 2..59) {
        return "$hour ${context.getString(R.string.ca_hours)}"
    }
    val day = hour / 24
    if (day in 0..1) {
        return "$day ${context.getString(R.string.ca_day)}"
    } else if (day in 2..59) {
        return "$day ${context.getString(R.string.ca_days)}"
    }
    return this.toString()
}

fun Long.millisToTimeString2(): String {
    return String.format(
        "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                this
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                this
            )
        )
    )
}

fun Long.toMoneyValue(charEnd: String = "đ"): String {
    val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(this).toString()
    return formattedValue.replace(",", ".") + charEnd
}

fun Long.toMoneyValueWithUnderline(charEnd: String = "đ"): Spanned {
    val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(this).replace(",", ".")
    return "$formattedValue <u>$charEnd</u>".toHtml()
}

fun Long.toMoneyValue(): String {
    return String.format("%,d", this).replace(',', '.')
}


fun Long.toMoneyValueWithPrefixUnderline(charPrefix: String = "đ"): Spanned {
    val formattedValue = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(this)

    return "<u>$charPrefix</u>$formattedValue".toHtml()
}

fun Long.formatFileSize(): String {
    val kiloByte = 1024L
    val megaByte = kiloByte * 1024
    val gigaByte = megaByte * 1024
    val teraByte = gigaByte * 1024

    return when {
        this < kiloByte -> "$this B"
        this < megaByte -> String.format("%.2f Kb", this.toFloat() / kiloByte)
        this < gigaByte -> String.format("%.2f Mb", this.toFloat() / megaByte)
        this < teraByte -> String.format("%.2f Gb", this.toFloat() / gigaByte)
        else -> String.format("%.2f Tb", this.toFloat() / teraByte)
    }
}