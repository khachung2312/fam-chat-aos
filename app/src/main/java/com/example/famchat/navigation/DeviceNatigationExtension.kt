package com.example.famchat.navigation

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.famchat.activity.BaseActivity
import com.example.famchat.activity.PermissionActivity
import java.io.File


/**
 * Open gallery photo
 * */
fun PermissionActivity.openGallery(
    allowMultiFile: Boolean = false,
    selectListener: (MutableList<Uri>) -> Unit
) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        openGalleryApi34(allowMultiFile) { uri ->
            selectListener.invoke(uri)
        }
        return
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    requestPermission({ success ->
        if (!success) {
            return@requestPermission
        }
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiFile)
        }
        launchActivityResult.launch(intent) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                getImageFromUriData(it.data, selectListener)
            }
        }
    }, permission)
}

private fun getImageFromUriData(data: Intent?, result: (MutableList<Uri>) -> Unit) {
    val selectedImages: MutableList<Uri> = mutableListOf()
    if (data?.clipData != null) {
        val clipData: ClipData = data.clipData!!
        for (i in 0 until clipData.itemCount) {
            val imageUri: Uri = clipData.getItemAt(i).uri
            selectedImages.add(imageUri)
        }
    } else if (data?.data != null) {
        val imageUri: Uri = data.data!!
        selectedImages.add(imageUri)
    }
    result(selectedImages)
}

/**
 * Open select file office from device
 * */
fun BaseActivity<*, *>.openSelectFileOfficeFromDevice(
    mineType: Array<String>,
    successListener: (MutableList<Uri>) -> Unit
) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mineType)
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    intent.type = "*/*"
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    launchActivityResult.launch(intent) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            getImageFromUriData(it.data, successListener)
        }
    }
}


/**
 * Open camera device
 * */
fun BaseActivity<*, *>.openCameraDevice(
    selectListener: (File, Uri) -> Unit
) {
    requestPermission(
        { permissionSuccess ->
            if (!permissionSuccess) {
                return@requestPermission
            }
            val file = File.createTempFile(
                "Image_${System.currentTimeMillis()}",
                ".png",
                cacheDir
            )
            val imageUri =
                FileProvider.getUriForFile(this, "${packageName}.file.provider", file)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            launchActivityResult.launch(cameraIntent) {
                if (it.resultCode == Activity.RESULT_OK) {
                    selectListener(file, imageUri)
                }
            }
        },
        arrayOf(android.Manifest.permission.CAMERA)
    )
}