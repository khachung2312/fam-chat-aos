package com.example.famchat.extensions

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun ByteArray.saveFile(fileSave: File): Boolean {
    if (fileSave.parentFile?.exists() == false) {
        fileSave.parentFile?.mkdirs()
    }
    val outputStream = FileOutputStream(fileSave)
    try {
        outputStream.write(this)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
    return true
}