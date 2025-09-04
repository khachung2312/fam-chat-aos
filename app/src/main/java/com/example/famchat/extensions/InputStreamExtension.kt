package com.example.famchat.extensions

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

fun InputStream.saveFile(fileSave: File): Boolean {
    if (fileSave.parentFile?.exists() == false) {
        fileSave.parentFile?.mkdirs()
    }
    val outputStream = FileOutputStream(fileSave)
    val buffer = ByteArray(1024)
    var bytesRead: Int
    try {
        while (this.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    } finally {
        this.close()
    }
    return true
}