package com.arclogbook.security

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GlobalErrorLogger {
    private val errorLog = mutableListOf<String>()

    fun logError(
        error: Throwable,
        context: String = "",
        userAction: String = "",
        deviceInfo: String = android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE,
        threadName: String = Thread.currentThread().name
    ) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())
        val logEntry = """
            [ERROR] $timestamp
            Context: $context
            User Action: $userAction
            Device Info: $deviceInfo
            Thread: $threadName
            Exception: ${error::class.java.name}
            Message: ${error.message}
            Stacktrace:
            ${error.stackTrace.joinToString("\n")}
        """.trimIndent()
        errorLog.add(logEntry)
        Log.e("GlobalErrorLogger", logEntry)
    }

    fun getAllErrors(): List<String> = errorLog.toList()
    fun clearErrors() = errorLog.clear()
}
