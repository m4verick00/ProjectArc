package com.arclogbook.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.viewmodel.DeepWebViewModel

@Composable
fun DeepWebMonitorScreen(viewModel: DeepWebViewModel = hiltViewModel()) {
    var keyword by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf("") }
    val alerts by viewModel.alerts.collectAsState()
    val sources = viewModel.sources

    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFFFF00FF).copy(alpha = 0.15f)))

    Column(Modifier.padding(16.dp)) {
        Text("Deep Web Monitoring Dashboard", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("Keyword to monitor") },
            modifier = Modifier.fillMaxWidth().animateContentSize()
        )
        Spacer(Modifier.height(8.dp))
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
        Button(
            onClick = { viewModel.searchDeepWeb(keyword, selectedSource) },
            enabled = keyword.isNotBlank() && selectedSource.isNotBlank(),
            modifier = Modifier.animateContentSize()
        ) {
            Text("Search Deep Web")
        }
        Spacer(Modifier.height(16.dp))
        if (alerts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LottieLoading()
                Text("No alerts yet!", style = MaterialTheme.typography.titleMedium, color = Color.Gray, modifier = Modifier.align(Alignment.BottomCenter).padding(top = 160.dp))
            }
        } else {
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
