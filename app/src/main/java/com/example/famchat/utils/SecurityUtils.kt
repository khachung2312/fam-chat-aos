package com.example.famchat.utils

import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.Gson
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    private const val seedValue = "A3qazzEDCTGBnhuijkl743349mkbogggjfh"
    private const val HEX = "0123456789ABCDEF"

    fun encrypt(cleartext: String): String {
        val rawKey = getRawKey(seedValue)
        val result = encrypt(rawKey, cleartext.toByteArray())
        return toHex(result)
    }

    fun decrypt(encrypted: String): String {
        val rawKey = getRawKey(seedValue)
        val enc: ByteArray = toByte(encrypted)
        val result = decrypt(rawKey, enc)
        return String(result)
    }

    private fun getRawKey(seed: String): ByteArray {
        val md = MessageDigest.getInstance("SHA-1")
        val digestOfPassword = md.digest(seed.toByteArray(charset("UTF-8")))
        return Arrays.copyOf(digestOfPassword, 24)
    }

    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    private fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    fun toHex(txt: String): String {
        return toHex(txt.toByteArray())
    }

    fun fromHex(hex: String): String {
        return String(toByte(hex))
    }

    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    private fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuilder(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private fun appendHex(sb: java.lang.StringBuilder, b: Byte) {
        sb.append(HEX.toCharArray()[b.toInt() shr 4 and 0x0f])
            .append(HEX.toCharArray()[b.toInt() and 0x0f])
    }

    @Keep
    fun decryptAESToClass(data: String?, sharedKey: String): String? {
        return try {
            val key = SecretKeySpec(Base64.decode(sharedKey, Base64.DEFAULT), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedBytes = Base64.decode(data, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            data
        }
    }

    @Keep
    fun encryptAESToClass(data: Any, sharedKey: String): String {
        return try {
            val key = SecretKeySpec(Base64.decode(sharedKey, Base64.DEFAULT), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val dataEncrypt = Gson().toJson(data)
            val decryptedBytes = cipher.doFinal(dataEncrypt.toByteArray())
            Base64.encodeToString(decryptedBytes, Base64.DEFAULT)
                .replace("\r".toRegex(), "")
                .replace("\n".toRegex(), "")
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

}