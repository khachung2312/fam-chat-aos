package com.example.famchat.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.Gravity
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity
import com.example.famchat.dialog.base.BaseDialog
import com.example.famchat.extensions.openStore

import java.util.Calendar

fun BaseActivity<*, *>.showDialogNotify(
    title: Int = R.string.fc_notification,
    message: String = "",
    icon: Int = 0,
    note: String = "",
    negativeText: Int? = null,
    positiveText: Int? = null,
    autoDismiss: Int = 0,
    isButtonVertical: Boolean = false,
    isSpanText: Boolean = false,
    spanText: String = "",
    onSpanTextListener: (() -> Unit)? = null,
    cancelable: Boolean = true,
    contentGravity: Int = Gravity.CENTER,
    isVisibleIconClose: Boolean = true,
    onClickCloseListener: (() -> Unit)? = null,
    positiveListener: (() -> Unit)? = null,
    negativeListener: (() -> Unit)? = null,
    getBuilder: (BaseDialog<*, *>?) -> Unit = {},
    dismissListener: (() -> Unit)? = null,
) {
    try {
//        val extendBuilder = DialogQuestion.ExtendBuilder(this)
//            .setIcon(icon)
//            .setNote(note)
//            .setOnClickCloseListener(onClickCloseListener)
//            .setButtonsVertical(isButtonVertical)
//            .setVisibleIconClose(isVisibleIconClose)
//            .setAutoDismiss(autoDismiss * 1000L)
//            .setContentGravity(contentGravity)
//            .setIsSpanText(isSpanText)
//            .setSpanText(spanText)
//            .setOnSpanTextListener(onSpanTextListener)
//            .setTitle(getString(title))
//            .setMessage(message.addDotLastString())
//            .setCancelable(cancelable)
//        negativeText?.let {
//            extendBuilder.onSetNegativeButton(getString(it)) { dialog ->
//                negativeListener?.invoke()
//                dialog.dismiss()
//            }
//        }
//        positiveText?.let {
//            extendBuilder.onSetPositiveButton(getString(it)) { dialog, _ ->
//                positiveListener?.invoke()
//                dialog.dismiss()
//            }
//        }
//        dismissListener?.let {
//            extendBuilder.onDismissListener {
//                dismissListener.invoke()
//            }
//        }
//        val builder = extendBuilder.build()
//        builder.show(supportFragmentManager, DialogQuestion::class.java.name)
//        getBuilder.invoke(builder)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


fun BaseActivity<*, *>.showDialogMaintenance(
    message: String?,
    forceCloseApp: Boolean = false,
    result: (() -> Unit)? = null
) {
//    showDialogNotify(
//        icon = R.drawable.ca_ic_warring,
//        message = message.toString(),
//        positiveText = R.string.ca_close,
//        contentGravity = Gravity.START,
//        dismissListener = {
//            if (forceCloseApp) {
//                finishAffinity()
//            }
//            result?.invoke()
//        }
//    )
}

fun BaseActivity<*, *>.showDialogOtpVerifyUser(
    phoneNumber: String? = null,
    email: String? = null,
//    result: (DialogOtpAuth, String?, String?) -> Unit
) {
//    try {
//        DialogOtpAuth.ExtendBuilder(this)
//            .setPhoneNumber(phoneNumber)
//            .setEmail(email)
//            .setTitle(getString(R.string.ca_user_auth))
//            .setCanOnTouchOutside(false)
//            .onSetNegativeButton(getString(R.string.ca_skip)) {
//                it.dismiss()
//            }
//            .onSetPositiveButton(getString(R.string.ca_txt_continue)) { dialog, data ->
//                val otpEmail = data[DialogOtpAuth.DATA_OTP_EMAIL] as String?
//                val otpPhone = data[DialogOtpAuth.DATA_OTP_PHONE] as String?
//                result(dialog as DialogOtpAuth, otpPhone, otpEmail)
//            }
//            .build()
//            .show(supportFragmentManager, DialogOtpAuth::javaClass.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
}

fun BaseActivity<*, *>.openDatePickerDialog(
    currentDate: Calendar,
    minDate: Calendar? = null,
    result: ((Calendar) -> Unit)? = null
) {
    try {
        val dialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                val calendarSelect = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                result?.invoke(calendarSelect)
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        minDate?.let {
            dialog.datePicker.minDate = it.timeInMillis
        }
        dialog.show()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun BaseActivity<*, *>.openTimePickerDialog(
    currentDate: Calendar,
    result: ((Calendar) -> Unit)? = null
) {
    try {
        TimePickerDialog(
            this, { _, hourOfDay, minute ->
                val calendarSelect = Calendar.getInstance().apply {
                    set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                    set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                result?.invoke(calendarSelect)
            },
            currentDate.get(Calendar.HOUR_OF_DAY),
            currentDate.get(Calendar.MINUTE),
            true
        ).show()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

//fun BaseActivity<*, *>.showDialogOtp(
//    result: (DialogOtp, String) -> Unit,
//    message: String? = null,
//    timeOtp: Int? = null,
//    remainTimeAllowRenew: Int? = null,
//    resendOtpListener: ((BaseDialog<*,*>) -> Unit)? = null
//) {
//    try {
//        DialogOtp.ExtendBuilder(this)
//            .setResendOtpListener {
//                resendOtpListener?.invoke(it)
//            }
//            .setTimeOtp(timeOtp)
//            .setRemainTimeAllowRenew(remainTimeAllowRenew)
//            .setMessage(message)
//            .setTitle(getString(R.string.ca_otp))
//            .setCanOnTouchOutside(false)
//            .onSetNegativeButton(getString(R.string.ca_close)) {
//                it.dismiss()
//            }
//            .onSetPositiveButton(getString(R.string.ca_confirm)) { dialog, data ->
//                val otpPhone = data[DialogOtp.DATA_OTP_PHONE] as String? ?: ""
//                result(dialog as DialogOtp, otpPhone)
//            }
//            .build()
//            .show(supportFragmentManager, DialogOtp::javaClass.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}

//fun BaseActivity<*, *>.showDialogWelCome(dismissListener: () -> Unit) {
//    try {
//        DialogWelcome.ExtendBuilder(this)
//            .onSetPositiveButton(getString(R.string.ca_start)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .onDismissListener {
//                dismissListener()
//            }
//            .build()
//            .show(supportFragmentManager, DialogWelcome::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//fun BaseActivity<*, *>.showDialogNewVersion(
//    message: String?,
//    enableOnDismiss: Boolean = false,
//    onDismiss: () -> Unit = {}
//) {
//    try {
//        DialogNewVersion.ExtendBuilder(this)
//            .setMessage(message)
//            .onSetPositiveButton(getString(R.string.ca_close)) { dialog, _ ->
//                if (!enableOnDismiss) onDismiss.invoke()
//                dialog.dismiss()
//            }
//            .onDismissListener {
//                if (enableOnDismiss) onDismiss.invoke()
//            }
//            .build()
//            .show(supportFragmentManager, DialogWelcome::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//
//fun BaseActivity<*, *>.showDialogReferral(invitationCode: String?, result: (String) -> Unit) {
//    try {
//        DialogReferral.ExtendBuilder(this)
//            .setReferralPhoneNumber(invitationCode)
//            .setSuccessListener {
//                result(it)
//            }
//            .onSetNegativeButton(getString(R.string.ca_skip)) { dialog ->
//                dialog.dismiss()
//            }
//            .build()
//            .show(supportFragmentManager, DialogReferral::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//fun BaseActivity<*, *>.showDialogCreateOrderSuccess(
//    orderId: String?,
//    autoDismiss: Long = 0L,
//    dismissListener: () -> Unit
//) {
//    try {
//        DialogCreateOrderSuccess.ExtendBuilder(this)
//            .setOrderId(orderId)
//            .setTimeAutoDismiss(autoDismiss * 1000L)
//            .onSetPositiveButton(getString(R.string.ca_close)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .onDismissListener {
//                dismissListener()
//            }
//            .build()
//            .show(supportFragmentManager, DialogCreateOrderSuccess::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//fun BaseActivity<*, *>.showDialogSelectCts(
//    lstCts: MutableList<CtsDetailResponse>,
//    dismissListener: (CtsDetailResponse) -> Unit
//) {
//    try {
//        DialogSelectCts.ExtendBuilder(this)
//            .setListCts(lstCts)
//            .onSetPositiveButton(getString(R.string.ca_txt_continue)) { dialog, data ->
//                val accountBalanceResponse =
//                    data[DialogSelectCts.DATA_CTS_SELECT] as CtsDetailResponse?
//                accountBalanceResponse?.let {
//                    dismissListener.invoke(it)
//                }
//                dialog.dismiss()
//            }
//            .build()
//            .show(supportFragmentManager, DialogSelectCts::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//
//fun BaseActivity<*, *>.showDialogSelectDate(
//    startDate: Calendar,
//    endDate: Calendar,
//    maxDate: Calendar? = null,
//    minDate: Calendar? = null,
//    dateSelectedListener: ((Calendar, Calendar) -> Unit)
//) {
//    try {
//        DialogSelectDate.ExtendBuilder(this)
//            .setStartDate(startDate)
//            .setEndDate(endDate)
//            .setMaxDate(maxDate)
//            .setMinDate(minDate)
//            .setDateSelectedListener { startDate, endDate ->
//                dateSelectedListener(startDate, endDate)
//            }
//            .onSetNegativeButton(getString(R.string.ca_back)) { dialog ->
//                dialog.dismiss()
//            }
//            .onSetPositiveButton(getString(R.string.ca_txt_continue)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .build()
//            .show(supportFragmentManager, DialogSelectDate::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//fun BaseActivity<*, *>.showDialogCancelOrder(
//    message: String,
//    txtBtn1: String? = null,
//    listenerBtn1: (() -> Unit)? = null,
//    txtBtn2: String? = null,
//    listenerBtn2: (() -> Unit)? = null,
//    txtBtn3: String? = null,
//    listenerBtn3: (() -> Unit)? = null
//) {
//    try {
//        DialogCancelOrder.ExtendBuilder(this)
//            .setButton1(txtBtn1, listenerBtn1)
//            .setButton2(txtBtn2, listenerBtn2)
//            .setButton3(txtBtn3, listenerBtn3)
//            .setMessage(message)
//            .setTitle(getString(R.string.ca_notification))
//            .build()
//            .show(supportFragmentManager, DialogSelectDate::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}
//
//fun BaseActivity<*, *>.showDialogSelectRegister(
//    listenerBtn1: (() -> Unit)? = null,
//    listenerBtn2: (() -> Unit)? = null,
//) {
//    try {
//        DialogSelectRegister.ExtendBuilder(this)
//            .setButton1(listenerBtn1)
//            .setButton2(listenerBtn2)
//            .build()
//            .show(supportFragmentManager, DialogSelectRegister::class.java.name)
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}