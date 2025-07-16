package com.arclogbook.security

import android.content.Context
import com.google.android.gms.safetynet.SafetyNet

object AppIntegrityUtils {
    fun attest(context: Context, onResult: (Boolean) -> Unit) {
        SafetyNet.getClient(context).attest("nonce".toByteArray(), "YOUR_API_KEY")
            .addOnSuccessListener { response ->
                // Parse response for integrity
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}
