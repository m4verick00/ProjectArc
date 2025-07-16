package com.arclogbook.security

import android.content.Context

object RemoteWipeUtils {
    fun remoteWipe(context: Context, command: String) {
        if (command == "WIPE_ARC") {
            // Call DataWipeUtils.wipeAppData
            com.arclogbook.security.DataWipeUtils.wipeAppData(context)
        }
    }
}
