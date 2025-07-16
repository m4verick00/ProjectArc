package com.arclogbook.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.arclogbook.ui.components.CyberpunkButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ResearchDisclaimerScreen() {
    Box(Modifier.fillMaxSize().pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            if (dragAmount < -50) {/* swipe left to acknowledge */}
        }
    }) {
        Column(Modifier.padding(16.dp)) {
            // Accessibility: dynamic font scaling
            val fontScale = LocalContext.current.resources.configuration.fontScale
            val scaledTitleSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale).sp

            Text("Research Mode Disclaimer", style = MaterialTheme.typography.titleLarge.copy(fontSize = scaledTitleSize, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(16.dp))
            Text("This app is intended for ethical research and educational purposes only.\n\n" +
                    "- Do not use for illegal activity.\n" +
                    "- Do not buy, sell, or distribute illegal data.\n" +
                    "- Always respect privacy and local laws.\n" +
                    "- Document your sources and methods.\n\n" +
                    "By using this app, you agree to use it responsibly and ethically.")
            Spacer(Modifier.height(32.dp))
            CyberpunkButton(
                onClick = { /* Maybe navigate back or acknowledge */ },
                text = "I Understand",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(Unit) {
        val context = LocalContext.current
        if (DeviceSecurityUtils.isEmulator()) {
            SecurityLogger.logSuspiciousActivity("App running on emulator")
        }
        if (DeviceSecurityUtils.isRooted()) {
            SecurityLogger.logSuspiciousActivity("Device is rooted")
        }
        val expectedSignature = "YOUR_BASE64_SIGNATURE" // Replace with your actual signature
        if (!SignatureUtils.isSignatureValid(context, expectedSignature)) {
            SecurityLogger.logSuspiciousActivity("App signature mismatch (possible tampering)")
        }
    }
}
