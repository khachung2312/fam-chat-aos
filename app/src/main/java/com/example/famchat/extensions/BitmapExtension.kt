package com.example.famchat.extensions

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Base64
import android.util.Size
import androidx.core.net.toUri
import java.io.*

fun Uri.uriToBitmap(context: Context): Bitmap? {
    return try {
        val input = context.contentResolver.openInputStream(this)
        val bitmap = BitmapFactory.decodeStream(input)
        input?.close()
        bitmap
    } catch (ex: Exception) {
        return try {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                context.contentResolver.openFileDescriptor(this, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
            image
        } catch (ex: Exception) {
            null
        }
    }
}

fun Bitmap.bitmapToUri(context: Context): Uri {
    /**Will create image in device*/
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String =
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            this,
            "image_" + System.currentTimeMillis(),
            null
        )
    return Uri.parse(path)
}

fun Bitmap.bitmapToUriCache(
    context: Context,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
): Uri {
    /**Only create image in cache application*/
    val fileType = when (format) {
        Bitmap.CompressFormat.PNG -> {
            "png"
        }

        Bitmap.CompressFormat.JPEG -> {
            "jpg"
        }

        Bitmap.CompressFormat.WEBP -> {
            "webp"
        }

        else -> {
            "png"
        }
    }
    val saveFile =
        File(context.cacheDir.path, "${System.currentTimeMillis()}.$fileType")
    try {
        val fos = FileOutputStream(saveFile)
        compress(format, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return saveFile.toUri()
}

fun Bitmap.bitmapToFileCache(context: Context): File {
    /**Only create image in cache application*/
    val saveFile =
        File(context.cacheDir.path, "${System.currentTimeMillis()}.png")
    try {
        val fos = FileOutputStream(saveFile)
        compress(Bitmap.CompressFormat.PNG, 80, fos)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return saveFile
}

fun Bitmap.scaleBitmap(widthScale: Float, heightScale: Float): Bitmap {
    val m = Matrix()
    m.setRectToRect(
        RectF(0f, 0f, width.toFloat(), height.toFloat()), RectF(
            0f, 0f,
            widthScale,
            heightScale
        ), Matrix.ScaleToFit.CENTER
    )
    return Bitmap.createBitmap(this, 0, 0, width, height, m, true)
}

fun Bitmap.cropBitmap(widthScale: Int, heightScale: Int): Bitmap {
    return ThumbnailUtils.extractThumbnail(
        this,
        widthScale,
        heightScale,
        ThumbnailUtils.OPTIONS_RECYCLE_INPUT
    )
}

fun Bitmap.orientationRotate(orientation: Int): Bitmap {
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
        Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, false)
    this.recycle()
    return bmRotated
}

fun Bitmap.rotateBitmap(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.autoRotate(context: Context): Bitmap {
    val inputStream = context.contentResolver.openInputStream(this.bitmapToUriCache(context))
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
            Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, false)
        this.recycle()
        return bmRotated
    } else {
        return this
    }
}

fun Bitmap.toByteArray(): ByteArray? {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    this.recycle()
    return byteArray
}

fun Bitmap.paddingBitmap(sizePading: Int): Bitmap? {
    val result = this.config?.let {
        Bitmap.createBitmap(
        this.width + (2 * sizePading),
        this.height + (2 * sizePading),
            it
    )
    }
    val canvas = result?.let { Canvas(it) }
    canvas?.drawColor(Color.WHITE)
    canvas?.drawBitmap(this, sizePading.toFloat(), sizePading.toFloat(), null)
    return result
}

fun Bitmap.resizeBitmapByCanvas(
    imageViewWidth: Float,
    imageViewHeight: Float,
    getMaxOriginal: Boolean = true
): Bitmap {
    val width: Float
    val height: Float
    val originalWidth = this.width.toFloat()
    val originalHeight = this.height.toFloat()
    if (originalWidth > originalHeight) {
        width = imageViewWidth
        height = imageViewWidth * originalHeight / originalWidth
    } else {
        height = imageViewHeight
        width = imageViewHeight * originalWidth / originalHeight
    }
    if (getMaxOriginal && (width > originalWidth || height > originalHeight)) {
        return this
    }
    val background = Bitmap.createBitmap(
        width.toInt(),
        height.toInt(), Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(background)
    val scale = width / originalWidth
    val yTranslation = (height - originalHeight * scale) / 2.0f
    val transformation = Matrix()
    transformation.postTranslate(0.0f, yTranslation)
    transformation.preScale(scale, scale)
    val paint = Paint()
    paint.isFilterBitmap = true
    canvas.drawBitmap(this, transformation, paint)
    return background
}


fun Bitmap.createBitmapQuality(quality: Int): Bitmap {
    val newWidth: Int
    val newHeight: Int
    if (width >= height) {
        val newH = quality * height / width
        newWidth = quality
        newHeight = newH
    } else {
        val newW = quality * width / height
        newWidth = newW
        newHeight = quality
    }
    val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val ratioX: Float = newWidth / this.width.toFloat()
    val ratioY: Float = newHeight / this.height.toFloat()
    val middleX: Float = newWidth / 2.0f
    val middleY: Float = newHeight / 2.0f

    val scaleMatrix = Matrix()
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

    val canvas = Canvas(scaledBitmap)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(
        this,
        middleX - this.width / 2,
        middleY - this.height / 2,
        Paint(Paint.FILTER_BITMAP_FLAG)
    )
    return scaledBitmap
}

fun Bitmap.getSizeFitScreen(context: Context): Size {
    val displayMetrics = context.getDisplayMetrics()
    return getSizeFitParent(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun Bitmap.getBitmapFitScreen(context: Context): Bitmap {
    val displayMetrics = context.getDisplayMetrics()
    val size = getSizeFitParent(displayMetrics.widthPixels, displayMetrics.heightPixels)
    return resizeBitmapByCanvas(size.width.toFloat(), size.height.toFloat())
}

fun Bitmap.getSizeFitParent(parentWidth: Int, parentHeight: Int): Size {
    var width: Int = width
    var height: Int = height
    val ratioX = parentWidth / width.toFloat()
    val ratioY = parentHeight / height.toFloat()

    if (ratioX > ratioY) {
        width = (width * ratioY).toInt()
        height = parentHeight
    } else {
        width = parentWidth
        height = (height * ratioX).toInt()
    }

    return Size(width, height)
}

fun Bitmap.mergeFront(frontBitmap: Bitmap): Bitmap? {
    val result = config?.let { Bitmap.createBitmap(width, height, it) }
    val canvas = result?.let { Canvas(it) }
    val widthBack: Int = width
    val widthFront: Int = frontBitmap.width
    val move = ((widthBack - widthFront) / 2).toFloat()
    canvas?.drawBitmap(this, 0f, 0f, null)
    canvas?.drawBitmap(frontBitmap, move, move, null)
    return result
}

fun Bitmap.getAspectRatio(): Float {
    return height.toFloat() / width.toFloat()
}

fun Bitmap.toThumbnail(context: Context, scale: Float = 0.2f): Bitmap {
    val thumbW = context.getDisplayMetrics().widthPixels.toFloat() * scale
    val thumbH = thumbW * getAspectRatio()
    return resizeBitmapByCanvas(thumbW, thumbH)
}

fun Bitmap.createBitmap(newWidth: Int, newHeight: Int): Bitmap {
    val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val ratioX: Float = newWidth / this.width.toFloat()
    val ratioY: Float = newHeight / this.height.toFloat()
    val middleX: Float = newWidth / 2.0f
    val middleY: Float = newHeight / 2.0f

    val scaleMatrix = Matrix()
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

    val canvas = Canvas(scaledBitmap)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(
        this,
        middleX - this.width / 2,
        middleY - this.height / 2,
        Paint(Paint.FILTER_BITMAP_FLAG)
    )

    return scaledBitmap
}

fun Bitmap.invertBitmap(): Bitmap? {
    val height = this.height
    val width = this.width
    val bitmap = this.config?.let { Bitmap.createBitmap(width, height, it) }
    val canvas = bitmap?.let { Canvas(it) }
    val paint = Paint()
    val matrixGrayscale = ColorMatrix()
    val matrixInvert = ColorMatrix()
    matrixInvert.set(
        floatArrayOf(
            -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
            0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
        )
    )
    matrixInvert.preConcat(matrixGrayscale)
    val filter = ColorMatrixColorFilter(matrixInvert)
    paint.colorFilter = filter
    canvas?.drawBitmap(this, 0f, 0f, paint)
    return bitmap
}


fun Bitmap?.toBase64(): String {
    if (this == null) {
        return ""
    }
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Bitmap?.toBase64NoWrap(): String {
    return toBase64()
        .replace("\r".toRegex(), "")
        .replace("\n".toRegex(), "")
}

fun Bitmap.cropWhiteSpace(
    cropTop: Boolean = true,
    cropBottom: Boolean = true,
    cropLeft: Boolean = true,
    cropRight: Boolean = true
): Bitmap {
    // Tìm biên của phần không trắng ở cạnh trái
    var left = 0
    if (cropLeft) {
        while (left < this.width && isWhiteSpaceColumn(this, left)) {
            left++
        }
    }

    // Tìm biên của phần không trắng ở cạnh phải
    var right = this.width - 1
    if (cropRight) {
        while (right >= 0 && isWhiteSpaceColumn(this, right)) {
            right--
        }
    }

    // Tìm biên của phần không trắng ở cạnh trên
    var top = 0
    if (cropTop) {
        while (top < this.height && isWhiteSpaceRow(this, top)) {
            top++
        }
    }

    // Tìm biên của phần không trắng ở cạnh dưới
    var bottom = this.height - 1
    if (cropBottom) {
        while (bottom >= 0 && isWhiteSpaceRow(this, bottom)) {
            bottom--
        }
    }

    // Kiểm tra xem có phải là ảnh trắng toàn bộ không
    if (left >= right || top >= bottom) {
        // Trả về một bitmap trắng
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    return Bitmap.createBitmap(this, left, top, right - left + 1, bottom - top + 1)
}

private fun isWhiteSpaceColumn(bitmap: Bitmap, column: Int): Boolean {
    for (y in 0 until bitmap.height) {
        if (bitmap.getPixel(column, y) != Color.TRANSPARENT) {
            return false
        }
    }
    return true
}

private fun isWhiteSpaceRow(bitmap: Bitmap, row: Int): Boolean {
    for (x in 0 until bitmap.width) {
        if (bitmap.getPixel(x, row) != Color.TRANSPARENT) {
            return false
        }
    }
    return true
}
