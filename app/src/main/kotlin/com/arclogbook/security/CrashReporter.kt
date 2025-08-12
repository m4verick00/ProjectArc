package com.arclogbook.security

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CrashReporter {
    private val crashLog = mutableListOf<String>()

    fun init(context: Context) {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())
            val logEntry = """
                [CRASH] $timestamp
                Thread: ${thread.name}
                Exception: ${throwable::class.java.name}
                Message: ${throwable.message}
                Stacktrace:
                ${throwable.stackTrace.joinToString("\n")}
            """.trimIndent()
            crashLog.add(logEntry)
            saveCrashLog(context, logEntry)
        }
    }

    private fun saveCrashLog(context: Context, log: String) {
        val file = File(context.filesDir, "crash_log.txt")
        file.appendText(log + "\n\n")
    }

    fun getAllCrashes(): List<String> = crashLog.toList()
    fun clearCrashes() = crashLog.clear()
}
