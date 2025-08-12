package com.arclogbook.security

import android.content.Context
import android.content.Intent
import android.net.Uri

object VpnTorUtils {
    fun launchOrbot(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.torproject.android"))
        context.startActivity(intent)
    }
}
