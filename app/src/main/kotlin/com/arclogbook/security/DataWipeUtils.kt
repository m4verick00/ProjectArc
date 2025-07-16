package com.arclogbook.security

import android.content.Context
import java.io.File

object DataWipeUtils {
    fun wipeAppData(context: Context) {
        val filesDir = context.filesDir
        filesDir.listFiles()?.forEach { it.deleteRecursively() }
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        // Optionally clear SharedPreferences
        val prefs = context.getSharedPreferences("arc_encrypted_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
