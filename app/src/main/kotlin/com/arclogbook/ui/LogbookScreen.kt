package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.viewmodel.LogbookViewModel
import com.arclogbook.viewmodel.OneDriveSyncViewModel
import com.arclogbook.data.LogEntry
import androidx.compose.ui.platform.LocalContext
import java.io.File
import androidx.navigation.NavController
import java.util.Calendar
import com.arclogbook.ui.util.rememberIsWideScreen
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.arclogbook.ui.LottieLoading
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.arclogbook.data.LogbookExportUtils
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.arclogbook.R
import com.arclogbook.ui.components.NeonProgressBar
import com.arclogbook.ui.components.CyberpunkButton
import com.arclogbook.ui.components.AnimatedCheckmark
import coil.compose.AsyncImage
import com.arclogbook.security.PermissionUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils
import com.arclogbook.security.EncryptedExportImportUtils
import com.arclogbook.ui.AdvancedSearchScreen
import com.arclogbook.ui.NoteTakingScreen
import com.arclogbook.ui.EvidenceManagementScreen
import com.arclogbook.ui.CaseTrackingScreen
import com.arclogbook.security.ChainOfCustodyUtils
import com.arclogbook.osint.GeoTaggingUtils
import com.arclogbook.osint.DarkWebMonitorUtils
import com.arclogbook.security.CustomAlertRules
import com.arclogbook.export.AdvancedExportUtils

@Composable
fun LogbookScreen(viewModel: LogbookViewModel = hiltViewModel(), syncViewModel: OneDriveSyncViewModel = hiltViewModel(), navController: NavController? = null) {
    val context = LocalContext.current
    var backupFrequency by remember { mutableStateOf("Day") }
    val backupOptions = listOf("Day", "Week", "Month", "Custom")
    var customDays by remember { mutableStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }
    var showStats by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("Date") }
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showVoiceInput by remember { mutableStateOf(false) }
    var voiceInputText by remember { mutableStateOf("") }
    var deleteCompleted by remember { mutableStateOf(false) }
    var editCompleted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var attachments by remember { mutableStateOf(listOf<String>()) } // URIs or file paths
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { attachments = attachments + it.toString() }
    }
    val storagePermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) imagePickerLauncher.launch("image/*")
    }
    // Placeholder search + filter state (actual data binding omitted for brevity in refactor scaffold)
    var search by remember { mutableStateOf("") }
    val entries by viewModel.logEntries.collectAsState()
    val filteredEntries: List<LogEntry> = remember(search, entries) {
        entries.filter { it.content.contains(search, true) || it.tags.contains(search, true) }
    }
    // Cyberpunk-styled logbook UI
    val isWide = rememberIsWideScreen()
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Logbook", fontWeight = FontWeight.Bold) }, actions = {
                IconButton(onClick = { /* search focus */ }) { Icon(Icons.Filled.Search, contentDescription = "Search") }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Add Entry") }
        }
    ) { inner ->
        if (!isWide) {
            Column(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    label = { Text("Search logs") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                LazyColumn(Modifier.fillMaxSize()) {
                    items(filteredEntries) { entry ->
                        ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Column(Modifier.padding(12.dp)) {
                                Text(entry.content, style = MaterialTheme.typography.bodyMedium)
                                Text(entry.tags, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        } else {
            Row(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp)
            ) {
                Column(Modifier.weight(0.45f).fillMaxHeight()) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        label = { Text("Search logs") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(filteredEntries) { entry ->
                            ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(entry.content, style = MaterialTheme.typography.bodyMedium)
                                    Text(entry.tags, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.width(16.dp))
                // Placeholder detail / stats panel
                Column(Modifier.weight(0.55f).fillMaxHeight()) {
                    Text("Detail / Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text("Select a log entry to view enriched metadata, attachments, chain-of-custody timeline, and AI summaries.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
