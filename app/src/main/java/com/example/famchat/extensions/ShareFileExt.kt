package com.example.famchat.extensions

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.famchat.R

const val MY_LIFE_PACKAGE_NAME = "com.vietteltelecom.mylife"
const val PDF_TYPE = "application/pdf"
const val TEXT_PLAIN_TYPE = "text/plain"

val extrasGetFileFromMyLife = listOf(
    "VIETTEL_APP" to "MY_SIGN",
    "FILE_SUPPORT" to "pdf,docx,xlsx,pptx,jpg,jpeg,png,gif,bmp,tiffLittle,tiffBig",
)

fun LocalizationActivity.shareFile(
    uri: Uri, packageName: String = MY_LIFE_PACKAGE_NAME, typeFile: String = PDF_TYPE
) {
    isInstalledApp(onSuccess = {
        kotlin.runCatching {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.apply {
                type = typeFile
                setPackage(packageName)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, uri)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent, getString(R.string.ca_day)
                )
            )
        }.onFailure {
            it.printStackTrace()
        }
    }, onFailure = {
        installAppInStore()
    })
}

fun LocalizationActivity.getFileFromOtherApp(
    resultLauncher: ActivityResultLauncher<Intent>,
    packageName: String = MY_LIFE_PACKAGE_NAME,
    extras: List<Pair<String, String>> = extrasGetFileFromMyLife,
    mimeType: String = TEXT_PLAIN_TYPE,
) {
    isInstalledApp(onSuccess = {
        kotlin.runCatching {
            val intent = Intent(Intent.ACTION_SEND).apply {
                setPackage(packageName)
                type = mimeType
                extras.forEach {
                    putExtra(it.first, it.second)
                }
            }
            resultLauncher.launch(
                Intent.createChooser(
                    intent, null
                )
            )
        }.onFailure {
            it.printStackTrace()
        }
    }, onFailure = {
        installAppInStore()
    })
}

@SuppressLint("QueryPermissionsNeeded")
fun LocalizationActivity.getAllAppDevice(): MutableList<ApplicationInfo> =
    packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

fun LocalizationActivity.isInstalledApp(
    packageAppName: String = MY_LIFE_PACKAGE_NAME,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {},
) {
    val isInstalled = getAllAppDevice().firstOrNull { it.packageName == packageAppName } != null
    if (isInstalled) onSuccess.invoke()
    else onFailure.invoke()
}

fun LocalizationActivity.installAppInStore(appPackageName: String = MY_LIFE_PACKAGE_NAME) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}