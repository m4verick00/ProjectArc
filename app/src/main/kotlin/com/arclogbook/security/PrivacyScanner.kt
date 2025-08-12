package com.arclogbook.security

object PrivacyScanner {
    fun scanForIdentifiers(data: String): List<String> {
        val patterns = listOf(
            "IMEI", "MAC", "Android ID", "Device ID", "Serial", "UUID", "Location"
        )
        return patterns.filter { data.contains(it, ignoreCase = true) }
    }
    fun scanCode(code: String): List<String> {
        val patterns = listOf(
            "android.provider.Settings.Secure.ANDROID_ID",
            "TelephonyManager.getDeviceId",
            "getMacAddress",
            "getSerial",
            "UUID.randomUUID()",
            "LocationManager",
            "BluetoothAdapter.getAddress"
        )
        return patterns.filter { code.contains(it, ignoreCase = true) }
    }
}
