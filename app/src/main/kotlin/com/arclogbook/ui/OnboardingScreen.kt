package com.arclogbook.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.arclogbook.R
import com.arclogbook.ui.components.NeonProgressBar
import com.arclogbook.ui.components.CyberpunkButton
import coil.compose.AsyncImage
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils

@Composable
fun OnboardingScreen(navController: NavController? = null) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cyberpunk_onboarding))
    val progress by animateLottieCompositionAsState(composition)
    var attachments by remember { mutableStateOf(listOf<String>()) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { attachments = attachments + it.toString() }
    }
    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                if (dragAmount < -50) navController?.navigate("logbook") // swipe left to continue
            }
        },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier.size(320.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "Welcome to ArcLogbook",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Your cyberpunk intelligence, log, and OSINT hub.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(16.dp))
            CyberpunkButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                text = "Add Profile Image",
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(8.dp))
            LazyRow {
                items(attachments) { attachment ->
                    AsyncImage(
                        model = attachment,
                        contentDescription = "Profile Attachment Image",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            CyberpunkButton(
                onClick = { navController?.navigate("logbook") },
                text = "Get Started",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(Unit) {
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
