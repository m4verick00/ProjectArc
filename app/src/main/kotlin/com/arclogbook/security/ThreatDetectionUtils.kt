package com.arclogbook.security

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ThreatDetectionUtils {
    fun isSuspiciousNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
    fun isAppTampered(): Boolean {
        // Example: check for tampering
        return false // Implement real checks
    }
}
