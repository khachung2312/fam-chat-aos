package com.example.famchat.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.famchat.R
import com.example.famchat.activity.BaseActivity
import com.example.famchat.utils.createPDF.CreatePdf
import com.example.famchat.utils.createPDF.OnPDFCreatedInterface
import com.example.famchat.utils.createPDF.PdfLib
import com.example.famchat.utils.createPDF.model.ImageToPDFOptions
import com.example.famchat.viewmodel.GlobalValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


fun File.shareFilePdf(context: Context) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.file.provider", this
        )
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.fc_close)
            )
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun File.openFile(context: Context) {
    try {
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.file.provider", this
        )
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = fileUri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun File.rotateImageIfNeeded(context: Context): File {
    val fileExt = FilenameUtils.getExtension(this.path)
    val file = File.createTempFile(
        "Image_${System.currentTimeMillis()}",
        ".$fileExt",
        context.cacheDir
    )
    try {
        val exif = ExifInterface(this.path)
        val rotation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        if (rotationInDegrees != 0) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(this.path, options)
            val width = options.outWidth
            val height = options.outHeight

            val matrix = Matrix()
            matrix.setRotate(rotationInDegrees.toFloat())

            val rotatedBitmap = Bitmap.createBitmap(
                BitmapFactory.decodeFile(this.path),
                0,
                0,
                width,
                height,
                matrix,
                true
            )

            val outputStream = FileOutputStream(file.path)
            rotatedBitmap.compress(
                if (fileExt == "png") {
                    Bitmap.CompressFormat.PNG
                } else {
                    Bitmap.CompressFormat.JPEG
                }, 100, outputStream
            )
            outputStream.flush()
            outputStream.close()
        } else {
            return this
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

fun File.toBitmap(): Bitmap? {
    return try {
        // Read the file into a byte array
        val fileInputStream = FileInputStream(this)
        val bytes = ByteArray(this.length().toInt())
        fileInputStream.read(bytes)
        fileInputStream.close()

        // Decode the byte array into a Bitmap
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun File.getUri(context: Context): Uri? {
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.file.provider", this
    )
}

fun LifecycleCoroutineScope.downloadFileWithLink(
    base64: String,
    fileFolder: File,
    fileName: String,
    typeFile: String,
    onDone: (String) -> Unit = {},
    onFailure: () -> Unit = {},
) {
    launch(Dispatchers.IO) {
        kotlin.runCatching {
            var mimeType = detectMimeType(base64).ifNullOrEmpty{ ".$typeFile" }
            var array = base64.base64ToByteArray()
            if (mimeType.contains("webp") || mimeType.contains("gif")) {
                mimeType = ".png"
                array = convertArrayImage(array, Bitmap.CompressFormat.PNG)
            }
            var fileNumber = 0
            val file = createFile(
                fileFolder,
                "$fileName$mimeType"
            ) { "$fileName(${++fileNumber})$mimeType" }
            FileUtils.writeByteArrayToFile(file, array)
            onDone.invoke(file.path)
        }.onFailure {
            it.printStackTrace()
            onFailure.invoke()
        }
    }
}

private fun convertArrayImage(
    arr: ByteArray,
    type: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): ByteArray {
    val bm = BitmapFactory.decodeByteArray(arr, 0, arr.size)
    val output = ByteArrayOutputStream()
    bm.compress(type, 100, output)
    val b: ByteArray = output.toByteArray()
    return b
}

val signatures = mapOf(
    "JVBERi0" to "application/pdf",
    "R0lGODdh" to "image/gif",
    "R0lGODlh" to "image/gif",
    "iVBORw0KGgo" to "image/png",
    "/9j/" to "image/jpg"
)

fun detectMimeType(b64: String): String {
    for ((signature, mimeType) in signatures) {
        if (b64.startsWith(signature)) {
            return ".${mimeType.split("/")[1]}"
        }
    }
    val startChar = b64.first().toString()
    return when (startChar) {
        "/" -> "jpg"
        "i" -> "png"
        "R" -> "gif"
        "U" -> "webp"
        else -> ""
    }
}

fun String.getMimeTypeFromLink(): String {
    var type = PDF_TYPE
    val ext = MimeTypeMap.getFileExtensionFromUrl(this)
    ext?.let {
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "*/pdf"
        kotlin.runCatching {
            type = ".${mimeType.split("/")[1]}"
        }
    }
    return type
}

fun createFile(
    fileFolder: File,
    fileName: String,
    newName: () -> String = { fileName }
): File {
    var file = File(fileFolder, fileName)
    if (file.exists()) file = createFile(fileFolder, newName.invoke(), newName)
    return file
}

fun LifecycleCoroutineScope.createPdf(
    context: Context,
    fileName: String,
    listPathUri: MutableList<Uri>? = null,
    listPathFile: MutableList<File>? = null,
    listener: OnPDFCreatedInterface,
) {
    launch(Dispatchers.IO) {
        val listPath = arrayListOf<String>()
        listPathUri?.forEach {
            val filePath = it.getImagePathFromContentUri(context)
            filePath?.let {
                listPath.add(
                    File(filePath).rotateImageIfNeeded(context).path
                )
            }
        }
        listPathFile?.forEach {
            listPath.add(it.path)
        }
        val options = ImageToPDFOptions().apply {
            imagesUri = listPath
            pageSize = PdfLib.DEFAULT_PAGE_SIZE_TEXT
            imageScaleType = PdfLib.IMAGE_SCALE_TYPE_ASPECT_RATIO
            pageColor = context.getColor(R.color.white)
            outFileName = fileName
        }
        CreatePdf(
            context,
            options,
            context.cacheDir.path.toString() + "/CreatePdf/",
            listener
        ).execute()
    }
}

