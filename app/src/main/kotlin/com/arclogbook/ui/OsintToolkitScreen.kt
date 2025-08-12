package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.viewmodel.OsintViewModel
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.arclogbook.ui.LottieLoading
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.arclogbook.ui.components.CyberpunkButton
import com.arclogbook.ui.components.NeonProgressBar
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import androidx.activity.result.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils
import com.arclogbook.osint.HaveIBeenPwnedApi
import com.arclogbook.osint.ShodanApi
import com.arclogbook.osint.CensysApi
import com.arclogbook.osint.VirusTotalApi
import com.arclogbook.osint.AbuseIpdbApi
import com.arclogbook.osint.RssFeedUtils
import com.arclogbook.osint.PastebinScraper
import com.arclogbook.security.EncryptedExportImportUtils
import com.arclogbook.security.OfflineModeUtils
import com.arclogbook.plugin.PluginManager
import com.arclogbook.ui.MapScreen
import com.arclogbook.osint.AutomatedWorkflowUtils
import com.arclogbook.security.ChainOfCustodyUtils
import com.arclogbook.osint.GeoTaggingUtils
import com.arclogbook.osint.DarkWebMonitorUtils
import com.arclogbook.security.CustomAlertRules
import androidx.compose.ui.platform.LocalContext
import com.arclogbook.osint.WorkflowWorker
import com.arclogbook.osint.WorkflowScheduler

@Composable
fun OsintToolkitScreen(viewModel: OsintViewModel = hiltViewModel()) {
    var target by remember { mutableStateOf("") }
    var queryType by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf("") }
    var apiKeyInput by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }
    val osintResults by viewModel.osintResults.collectAsState()
    val sources = viewModel.availableSources
    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF00FFFF).copy(alpha = 0.15f)))
    var attachments by remember { mutableStateOf(listOf<String>()) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { attachments = attachments + it.toString() }
    }
    var showStats by remember { mutableStateOf(false) }
    val statsData = osintResults.groupBy { it.source }.map { (source, results) ->
        Point(results.size.toFloat(), label = source)
    }
    val serviceOptions = listOf(
        "VirusTotal", "Shodan", "HaveIBeenPwned", "Censys", "AbuseIPDB", "Pastebin"
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(cardGradient)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { showStats = !showStats },
                    onLongPress = { /* Show context menu for result */ }
                )
            }
            .animateContentSize()
    ) {
        Column(Modifier.padding(16.dp)) {
            val fontScale = LocalContext.current.resources.configuration.fontScale
            val scaledTitleSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale).sp
            Text("OSINT Automation Toolkit", style = MaterialTheme.typography.titleLarge.copy(fontSize = scaledTitleSize, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(16.dp))
            CyberpunkButton(onClick = { imagePickerLauncher.launch("image/*") }, text = "Add Attachment", color = MaterialTheme.colorScheme.primary)
            CyberpunkButton(onClick = { showStats = !showStats }, text = "Show Stats", color = MaterialTheme.colorScheme.secondary)
            // Service dropdown
            Text("Select OSINT Service", style = MaterialTheme.typography.labelLarge)
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth().background(Color(0xFF23272E)),
            ) {
                serviceOptions.forEach { service ->
                    DropdownMenuItem(
                        text = { Text(service) },
                        onClick = { selectedService = service }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            // API key/address input bar
            OutlinedTextField(
                value = apiKeyInput,
                onValueChange = { apiKeyInput = it },
                label = { Text("Enter API Key / Address / Query") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF00FFFF),
                    unfocusedBorderColor = Color(0xFF23272E),
                    textColor = Color(0xFF00FFFF)
                )
            )
            Spacer(Modifier.height(8.dp))
            CyberpunkButton(
                onClick = {
                    viewModel.saveApiKey(selectedService, apiKeyInput)
                },
                text = "Save API Key / Address",
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            // Results list
            LazyColumn {
                items(osintResults) { result ->
                    // Cyberpunk-styled result card
                }
            }
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
