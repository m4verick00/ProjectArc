package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.arclogbook.ui.LottieLoading
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.arclogbook.data.LogbookExportUtils

@Composable
fun LogbookScreen(viewModel: LogbookViewModel = hiltViewModel(), syncViewModel: OneDriveSyncViewModel = hiltViewModel(), navController: NavController? = null) {
    val logEntries by viewModel.logEntries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filterTag by remember { mutableStateOf("") }
    var expandedEntryId by remember { mutableStateOf<Int?>(null) }
    val syncStatus by syncViewModel.syncStatus.collectAsState()
    val context = LocalContext.current
    var backupFrequency by remember { mutableStateOf("Day") }
    val backupOptions = listOf("Day", "Week", "Month", "Custom")
    var customDays by remember { mutableStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }
    var showStats by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("Date") }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val cardShape: Shape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF39FF14).copy(alpha = 0.15f)))

    Column(Modifier.padding(16.dp)) {
        Text("Logbook", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier.weight(1f).animateContentSize()
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = filterTag,
                onValueChange = { filterTag = it },
                label = { Text("Tag") },
                modifier = Modifier.weight(1f).animateContentSize()
            )
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Date Filter")
            }
            IconButton(onClick = { showStats = !showStats }) {
                Icon(Icons.Default.Insights, contentDescription = "Stats")
            }
            IconButton(onClick = { showFavoritesOnly = !showFavoritesOnly }) {
                Icon(if (showFavoritesOnly) Icons.Default.Star else Icons.Default.StarBorder, contentDescription = "Favorites")
            }
            IconButton(onClick = { sortBy = if (sortBy == "Date") "Type" else "Date" }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort")
            }
        }
        if (showStats) {
            // Show quick stats/insights
            val typeCounts = logEntries.groupBy { it.type }.mapValues { it.value.size }
            val tagCounts = logEntries.flatMap { it.tags.split(",") }.groupingBy { it.trim() }.eachCount().filter { it.key.isNotBlank() }
            Column(Modifier.padding(8.dp)) {
                Text("Entries by Type: ${typeCounts}", style = MaterialTheme.typography.labelMedium)
                Text("Tags: ${tagCounts}", style = MaterialTheme.typography.labelMedium)
            }
        }
        if (showDatePicker) {
            // TODO: Add a date range picker dialog and update dateRange
        }
        Spacer(Modifier.height(8.dp))
        if (logEntries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LottieLoading()
                Text("No log entries yet!", style = MaterialTheme.typography.titleMedium, color = Color.Gray, modifier = Modifier.align(Alignment.BottomCenter).padding(top = 160.dp))
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(logEntries.filter {
                    (searchQuery.isBlank() || it.content.contains(searchQuery, true) || it.tags.contains(searchQuery, true)) &&
                    (filterTag.isBlank() || it.tags.contains(filterTag, true)) &&
                    (!showFavoritesOnly || /* TODO: check favorite flag */ true) &&
                    (dateRange.first == null || it.timestamp >= dateRange.first!!) &&
                    (dateRange.second == null || it.timestamp <= dateRange.second!!)
                }.sortedWith(compareBy(if (sortBy == "Date") { { -it.timestamp } } else { { it.type } }))) { entry ->
                    var isExpanded by remember { mutableStateOf(expandedEntryId == entry.id) }
                    val motionScene = remember {
                        MotionScene("""
                        {
                          ConstraintSets: {
                            collapsed: {
                              card: { width: 'spread', height: 80, translationZ: 0 }
                            },
                            expanded: {
                              card: { width: 'spread', height: 220, translationZ: 16 }
                            }
                          },
                          Transitions: {
                            default: {
                              from: 'collapsed', to: 'expanded', pathMotionArc: 'startVertical', duration: 400
                            }
                          }
                        }
                        """)
                    }
                    MotionLayout(
                        motionScene = motionScene,
                        progress = if (isExpanded) 1f else 0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Card(
                            Modifier
                                .layoutId("card")
                                .fillMaxWidth()
                                .clickable { expandedEntryId = if (isExpanded) null else entry.id },
                            shape = cardShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 16.dp else 4.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(entry.type, color = Color(0xFF39FF14), fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(8.dp))
                                    Text(entry.tags, style = MaterialTheme.typography.labelMedium)
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(entry.timestamp)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(entry.content, maxLines = if (isExpanded) Int.MAX_VALUE else 2)
                                if (isExpanded) {
                                    Spacer(Modifier.height(8.dp))
                                    Row {
                                        IconButton(onClick = { viewModel.deleteLog(entry) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                        IconButton(onClick = { /* TODO: Edit entry */ }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = {
                                            navController?.navigate("logentrydetail/${entry.id}")
                                        }) {
                                            Icon(Icons.Default.Description, contentDescription = "View Result")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { /* TODO: Add new entry dialog */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Add New Entry")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { /* TODO: Export/Sync/Settings */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Export / Sync / Settings")
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Backup Frequency:", modifier = Modifier.padding(end = 8.dp))
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = backupFrequency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frequency") },
                    modifier = Modifier.menuAnchor().width(120.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    backupOptions.forEach { option =>
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                backupFrequency = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            if (backupFrequency == "Custom") {
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = customDays.toString(),
                    onValueChange = { v -> v.toIntOrNull()?.let { customDays = it } },
                    label = { Text("Days") },
                    modifier = Modifier.width(80.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(onClick = {
                val file = File(context.cacheDir, "ArcLogbookBackup.json")
                // TODO: Serialize logEntries to JSON and write to file
                val infoType = if (filterTag.isNotBlank()) filterTag else "All"
                syncViewModel.uploadBackup(file, infoType)
            }, modifier = Modifier.weight(1f)) {
                Text("Backup to OneDrive")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val file = File(context.cacheDir, "ArcLogbookBackup.json")
                syncViewModel.downloadBackup(file)
                // TODO: Deserialize JSON and restore to DB
            }, modifier = Modifier.weight(1f)) {
                Text("Restore from OneDrive")
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(onClick = {
                val file = LogbookExportUtils.exportToJson(context, logEntries)
                // TODO: Share or save file as needed
            }, modifier = Modifier.weight(1f)) {
                Text("Export as JSON")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val file = LogbookExportUtils.exportToPdf(context, logEntries)
                // TODO: Share or save file as needed
            }, modifier = Modifier.weight(1f)) {
                Text("Export as PDF")
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(syncStatus, style = MaterialTheme.typography.labelSmall, color = Color.Cyan)
    }
}
