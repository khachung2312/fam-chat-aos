package com.example.famchat.extensions

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.File
import java.io.IOException

fun Uri.autoRotate(context: Context): Uri {
    val inputStream = context.contentResolver.openInputStream(this)
    this.uriToBitmap(context)?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val exif = ExifInterface(inputStream!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> return this
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                else -> return this
            }
            val bmRotated =
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, false)
            it.recycle()
            return bmRotated.bitmapToUriCache(context)
        }
        return it.bitmapToUriCache(context)
    }
    return this
}

fun Uri.toBase64(context: Context): String {
    try {
        val bytes = context.contentResolver.openInputStream(this)?.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (error: IOException) {
        error.printStackTrace()
    }
    return ""
}

fun Uri.autoRotateBitmap(context: Context): Bitmap? {
    val inputStream = context.contentResolver.openInputStream(this)
    this.uriToBitmap(context)?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val exif = ExifInterface(inputStream!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> return null
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                else -> return it
            }
            val bmRotated =
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, false)
            it.recycle()
            return bmRotated
        }
        return it
    }
    return null
}

fun Uri.getImagePathFromContentUri(context: Context): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    var cursor: Cursor? = null
    var filePath: String? = null

    try {
        cursor = context.contentResolver.query(this, projection, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }
    return filePath
}


fun getFilePathFromUri(context: Context, uri: Uri): String? {
    var filePath: String? = null

    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        try {
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    filePath = cursor.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        filePath = uri.path
    }

    // Nếu là URI từ file manager của OPPO hoặc các file manager khác
    if (filePath == null && uri.path != null && uri.path!!.contains("/storage/emulated/")) {
        filePath = uri.path!!.replace("/root", "")
    }

    return filePath
}
fun getMimeTypeFromFilePath(filePath: String): String? {
    val fileExtension = File(filePath).extension
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
}





