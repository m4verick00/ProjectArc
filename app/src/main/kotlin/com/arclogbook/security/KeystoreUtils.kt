package com.arclogbook.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeystoreUtils {
    private const val KEY_ALIAS = "ArcLogbookKey"
    fun getOrCreateKey(context: Context): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val spec = KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

    // Example: Secure API key storage
    fun encryptApiKey(context: Context, apiKey: String): ByteArray {
        val key = KeystoreUtils.getOrCreateKey(context)
        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(apiKey.toByteArray())
    }
    fun decryptApiKey(context: Context, encrypted: ByteArray): String {
        val key = KeystoreUtils.getOrCreateKey(context)
        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(encrypted))
    }

    // Obfuscate sensitive strings
    fun obfuscateString(input: String): String {
        return input.reversed() // Simple example, use a stronger method in production
    }
    fun deobfuscateString(input: String): String {
        return input.reversed()
    }
}
