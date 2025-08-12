package com.arclogbook.security

import android.util.Log

object SecurityLogger {
    fun logSuspiciousActivity(reason: String) {
        // Only log generic info, never sensitive data
        Log.w("SecurityLogger", "Suspicious activity detected: $reason")
    }
}
