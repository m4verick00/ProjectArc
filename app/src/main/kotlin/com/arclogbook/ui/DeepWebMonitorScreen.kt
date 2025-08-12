package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Offset
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.components.NeonProgressBar
import com.arclogbook.ui.components.CyberpunkButton
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage
import com.arclogbook.viewmodel.DeepWebViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils

@Composable
fun DeepWebMonitorScreen(viewModel: DeepWebViewModel = hiltViewModel()) {
    var keyword by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf("") }
    var showStats by remember { mutableStateOf(false) }
    var attachments by remember { mutableStateOf(listOf<String>()) }
    val fontScale = LocalContext.current.resources.configuration.fontScale
    val scaledTitleSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale).sp
    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(Color(0xFF181A20), Color(0xFF23272E), Color(0xFF00FFFF).copy(alpha = 0.15f)))
            )
            .padding(16.dp)
            .animateContentSize()
    ) {
        Text("Deep Web Monitor", style = MaterialTheme.typography.titleLarge.copy(fontSize = scaledTitleSize, fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        TextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("Keyword") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = selectedSource,
            onValueChange = { selectedSource = it },
            label = { Text("Source") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))
        CyberpunkButton(
            onClick = { /* Start monitoring logic */ },
            text = "Monitor",
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        NeonProgressBar(progress = 0.5f)
        // Attachments preview
        LazyRow {
            items(attachments) { uri ->
                AsyncImage(model = uri, contentDescription = "Attachment", modifier = Modifier.size(64.dp))
            }
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
