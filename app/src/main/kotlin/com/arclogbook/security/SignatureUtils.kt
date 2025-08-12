package com.arclogbook.security

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64

object SignatureUtils {
    fun isSignatureValid(context: Context, expectedSignature: String): Boolean {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        val signatures = packageInfo.signingInfo.apkContentsSigners
        val actualSignature = Base64.encodeToString(signatures[0].toByteArray(), Base64.DEFAULT)
        return actualSignature == expectedSignature
    }
}
