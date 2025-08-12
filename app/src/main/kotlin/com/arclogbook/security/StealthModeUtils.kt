package com.arclogbook.security

import android.content.Context
import android.content.pm.PackageManager

object StealthModeUtils {
    fun hideAppIcon(context: Context) {
        val pm = context.packageManager
        val componentName = context.packageName + ".MainActivity"
        pm.setComponentEnabledSetting(
            android.content.ComponentName(context, componentName),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
    fun showAppIcon(context: Context) {
        val pm = context.packageManager
        val componentName = context.packageName + ".MainActivity"
        pm.setComponentEnabledSetting(
            android.content.ComponentName(context, componentName),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
