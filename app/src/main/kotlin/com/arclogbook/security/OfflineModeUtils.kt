package com.arclogbook.security

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object OfflineModeUtils {
    fun isOffline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return true
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return true
        return !(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }
}
