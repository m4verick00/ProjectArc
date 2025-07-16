package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Offset
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.components.CyberpunkButton
import com.arclogbook.ui.components.NeonProgressBar
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage
import com.arclogbook.viewmodel.PastebinViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils
import com.arclogbook.osint.PastebinScraper

@Composable
fun PastebinMonitorScreen(viewModel: PastebinViewModel = hiltViewModel()) {
    var keyword by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf(listOf<String>()) }
    var showStats by remember { mutableStateOf(false) }
    val alerts by viewModel.alerts.collectAsState()
    val sources = viewModel.sources
    val statsData = alerts.groupBy { it.source }.map { (source, results) ->
        Point(results.size.toFloat(), label = source)
    }

    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF39FF14).copy(alpha = 0.15f)))

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { attachments = attachments + it.toString() }
    }

    // Accessibility: dynamic font scaling
    val fontScale = LocalContext.current.resources.configuration.fontScale
    val scaledTitleSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale).sp

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

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF181A20), Color(0xFF23272E), Color(0xFF00FFFF).copy(alpha = 0.08f))
                )
            )
            .padding(24.dp)
            .animateContentSize()
    ) {
        Column {
            Text(
                "Pastebin Monitor",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFFFF00FF),
                    shadow = Shadow(Color(0xFF00FFFF), Offset(2f, 2f), 8f)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Keyword", color = Color(0xFF00FFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF23272E),
                    focusedIndicatorColor = Color(0xFF39FF14),
                    unfocusedIndicatorColor = Color(0xFF00FFFF)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Source dropdown
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedSource,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Source") },
                    modifier = Modifier.menuAnchor().fillMaxWidth().animateContentSize(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    sources.forEach { source ->
                        DropdownMenuItem(
                            text = { Text(source) },
                            onClick = {
                                selectedSource = source
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            CyberpunkButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                text = "Add Attachment",
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(8.dp))
            LazyRow {
                items(attachments) { attachment ->
                    AsyncImage(
                        model = attachment,
                        contentDescription = "Pastebin Attachment Image",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            CyberpunkButton(
                onClick = { viewModel.startMonitoring(keyword, selectedSource) },
                text = "Start Monitoring",
                color = MaterialTheme.colorScheme.primary
            )
            CyberpunkButton(onClick = {
                val pastes = PastebinScraper.fetchLatestPastes()
                // Show in dialog or add to alerts
            }, text = "Scrape Pastebin", color = MaterialTheme.colorScheme.primary)
            if (viewModel.isLoading) {
                NeonProgressBar(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), color = Color(0xFF39FF14))
            }
            if (showStats) {
                LineChart(
                    lineChartData = LineChartData(points = statsData),
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            // Empty state
            if (alerts.isEmpty()) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    LottieLoading()
                    Text("No alerts yet!", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                }
            }
            // Error handling
            if (viewModel.errorMessage.isNotBlank()) {
                Text(viewModel.errorMessage, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }
            if (alerts.isNotEmpty()) {
                Column {
                    Text("Alerts:", style = MaterialTheme.typography.titleMedium)
                    alerts.forEach { alert ->
                        var expandedCard by remember { mutableStateOf(false) }
                        val motionScene = remember {
                            MotionScene("""
                            {
                              ConstraintSets: {
                                collapsed: { card: { width: 'spread', height: 80, translationZ: 0 } },
                                expanded: { card: { width: 'spread', height: 220, translationZ: 16 } }
                              },
                              Transitions: {
                                default: { from: 'collapsed', to: 'expanded', pathMotionArc: 'startVertical', duration: 400 }
                              }
                            }
                            """)
                        }
                        MotionLayout(
                            motionScene = motionScene,
                            progress = if (expandedCard) 1f else 0f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Card(
                                Modifier
                                    .layoutId("card")
                                    .fillMaxWidth()
                                    .shadow(if (expandedCard) 16.dp else 4.dp, cardShape)
                                    .clip(cardShape)
                                    .background(cardGradient)
                                    .clickable { expandedCard = !expandedCard },
                                shape = cardShape,
                                elevation = CardDefaults.cardElevation(defaultElevation = if (expandedCard) 16.dp else 4.dp)
                            ) {
                                Column(Modifier.padding(8.dp)) {
                                    Text("[${alert.source}] ${alert.snippet}")
                                    if (expandedCard) {
                                        Spacer(Modifier.height(4.dp))
                                        Button(
                                            onClick = { viewModel.saveAlertToLogbook(alert) },
                                            modifier = Modifier.animateContentSize()
                                        ) {
                                            Text("Save to Logbook")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
