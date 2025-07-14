package com.arclogbook.ui

import android.app.Activity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun BiometricUnlockScreen(onUnlock: () -> Unit, onError: (String) -> Unit) {
    val context = LocalContext.current
    var showPrompt by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf("") }

    if (showPrompt) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Logbook")
            .setSubtitle("Authenticate to access your logs")
            .setNegativeButtonText("Cancel")
            .build()
        val biometricPrompt = BiometricPrompt(context as Activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    showPrompt = false
                    onUnlock()
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    showPrompt = false
                    errorMsg = errString.toString()
                    onError(errString.toString())
                }
                override fun onAuthenticationFailed() {
                    errorMsg = "Authentication failed. Try again."
                }
            })
        LaunchedEffect(Unit) {
            biometricPrompt.authenticate(promptInfo)
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (errorMsg.isNotBlank()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        } else {
            CircularProgressIndicator()
        }
    }
}
