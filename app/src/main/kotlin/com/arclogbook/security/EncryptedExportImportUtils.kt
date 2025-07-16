package com.arclogbook.security

import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import java.io.File
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptedExportImportUtils {
    fun exportData(context: Context, data: ByteArray, file: File, key: ByteArray, iv: ByteArray) {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(data)
        file.writeBytes(encrypted)
    }

    fun importData(context: Context, file: File, key: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encrypted = file.readBytes()
        return cipher.doFinal(encrypted)
    }
}
