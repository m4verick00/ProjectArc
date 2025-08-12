package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arclogbook.viewmodel.LogbookViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.LottieLoading
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.arclogbook.plugin.PluginManager
import com.arclogbook.security.OfflineModeUtils
import com.arclogbook.voice.CyberpunkVoiceAssistant
import com.arclogbook.ui.widget.QuickLogWidget
import com.arclogbook.ui.MultiInstanceManager
import com.arclogbook.security.AccessibilityAuditUtils
import com.arclogbook.devtools.InAppCodeEditor
import com.arclogbook.devtools.ApiExplorer
import androidx.compose.ui.platform.LocalContext
import com.arclogbook.ui.TimelineMapScreen
import com.arclogbook.report.ReportGenerator
import com.arclogbook.collab.CollaborationUtils
import com.arclogbook.threatintel.ThreatIntelFeeds
import com.arclogbook.automation.ScriptEngine
import com.arclogbook.ui.VisualWorkflowBuilderScreen
import androidx.compose.ui.text.font.FontWeight
import com.arclogbook.analytics.LoggingAnalyticsDashboard
import com.arclogbook.plugin.PluginMarketplace

@Composable
fun DashboardScreen(viewModel: LogbookViewModel = hiltViewModel()) {
    val logEntries = viewModel.logEntries.collectAsState().value
    val typeCounts = logEntries.groupBy { it.type }.mapValues { it.value.size }
    val tagCounts = logEntries.flatMap { it.tags.split(",") }.groupingBy { it.trim() }.eachCount().filter { it.key.isNotBlank() }
    val recent = logEntries.sortedByDescending { it.timestamp }.take(5)
    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF00FFFF).copy(alpha = 0.15f)))
    if (logEntries.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LottieLoading()
            Text("No data yet!", style = MaterialTheme.typography.titleMedium, color = Color.Gray, modifier = Modifier.align(Alignment.BottomCenter).padding(top = 160.dp))
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            Text("Dashboard", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Divider()
            CyberpunkButton(onClick = {
                CyberpunkVoiceAssistant.startListening(LocalContext.current) { command ->
                    // Handle voice command
                }
            }, text = "Voice Assistant", color = MaterialTheme.colorScheme.primary)
            CyberpunkButton(onClick = {
                // Quick log widget logic (simulate)
            }, text = "Quick Log Widget", color = MaterialTheme.colorScheme.secondary)
            CyberpunkButton(onClick = {
                MultiInstanceManager.createInstance("Investigation ${System.currentTimeMillis()}")
            }, text = "New Investigation Instance", color = MaterialTheme.colorScheme.tertiary)
            CyberpunkButton(onClick = {
                val instances = MultiInstanceManager.getInstances()
                // Show instances in dialog/snackbar
            }, text = "Show Instances", color = MaterialTheme.colorScheme.primary)
            CyberpunkButton(onClick = {
                val audit = AccessibilityAuditUtils.audit(listOf("", "Logbook entry", ""))
                // Show audit results in dialog/snackbar
            }, text = "Accessibility Audit", color = MaterialTheme.colorScheme.secondary)
            CyberpunkButton(onClick = {
                // Show in-app code editor
            }, text = "Code Editor", color = MaterialTheme.colorScheme.tertiary)
            CyberpunkButton(onClick = {
                // Show API explorer
            }, text = "API Explorer", color = MaterialTheme.colorScheme.primary)
            CyberpunkButton(onClick = {
                // Show timeline/map view
                TimelineMapScreen()
            }, text = "Timeline/Map View", color = MaterialTheme.colorScheme.primary)
            CyberpunkButton(onClick = {
                // Show analytics dashboard
                LoggingAnalyticsDashboard.log("Dashboard viewed")
            }, text = "Analytics Dashboard", color = MaterialTheme.colorScheme.secondary)
            CyberpunkButton(onClick = {
                // Show plugin marketplace
                PluginMarketplace.discoverPlugins()
            }, text = "Plugin Marketplace", color = MaterialTheme.colorScheme.tertiary)
        }
    }
}
