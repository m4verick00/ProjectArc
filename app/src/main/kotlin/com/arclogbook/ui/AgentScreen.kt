package com.arclogbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.viewmodel.AgentViewModel
import com.arclogbook.viewmodel.AgentViewModel.AgentState
import com.arclogbook.viewmodel.AgentViewModel.AgentStep
import com.arclogbook.viewmodel.AgentViewModel.StepStatus
import com.arclogbook.ui.components.ButtonVariant
import com.arclogbook.ui.components.CyberpunkButton
import com.arclogbook.ui.components.SnackbarEvents
import kotlinx.coroutines.launch

@Composable
fun AgentScreen(viewModel: AgentViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val error by viewModel.error.collectAsState()
    val agentLogs by viewModel.agentLogs.collectAsState()

    var input by remember { mutableStateOf("") }
    var showLogs by remember { mutableStateOf(false) }
    var showCancelConfirm by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val logListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarScope = rememberCoroutineScope()

    val neon = MaterialTheme.colorScheme.primary
    val bg = Brush.verticalGradient(listOf(Color(0xFF0C1015), Color(0xFF141C24), neon.copy(alpha = 0.10f)))

    // Auto scroll to newest step
    LaunchedEffect(steps.size) { if (steps.isNotEmpty()) listState.animateScrollToItem(steps.size - 1) }
    // Auto scroll logs
    LaunchedEffect(agentLogs.size) { if (agentLogs.isNotEmpty() && showLogs) logListState.scrollToItem(0) }
    // Error snackbar
    LaunchedEffect(error) { error?.let { SnackbarEvents.dispatch("Agent error: $it", kind = com.arclogbook.ui.components.Kind.Error) } }

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().background(bg).padding(padding).padding(16.dp)) {
            Text("Autonomous OSINT Agent", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = neon)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Objective (e.g., Analyze domain example.com for leaks)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CyberpunkButton(
                    onClick = {
                        if (state == AgentState.RUNNING) {
                            showCancelConfirm = true
                        } else {
                            viewModel.startAgent(input.trim())
                            snackbarScope.launch { SnackbarEvents.dispatch("Agent started") }
                        }
                    },
                    text = if (state == AgentState.RUNNING) "Stop" else "Start",
                    color = neon,
                    variant = if (state == AgentState.RUNNING) ButtonVariant.Outlined else ButtonVariant.Filled,
                    enabled = input.isNotBlank() || state == AgentState.RUNNING
                )
                CyberpunkButton(
                    onClick = {
                        viewModel.clear();
                        snackbarScope.launch { SnackbarEvents.dispatch("Agent cleared") }
                    },
                    text = "Clear",
                    color = MaterialTheme.colorScheme.secondary,
                    variant = ButtonVariant.Tonal,
                    enabled = steps.isNotEmpty()
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state == AgentState.PLANNING || state == AgentState.RUNNING) {
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.weight(1f), color = neon)
                } else {
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.width(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showLogs = !showLogs }) {
                    Text("Logs", color = Color(0xFFB5C6D6), style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.width(4.dp))
                    Switch(checked = showLogs, onCheckedChange = { showLogs = it }, colors = SwitchDefaults.colors(checkedThumbColor = neon))
                }
            }
            Spacer(Modifier.height(8.dp))
            if (error != null) {
                Text(error ?: "", color = Color(0xFFFF5555), style = MaterialTheme.typography.bodySmall)
            }
            LazyColumn(
                Modifier
                    .weight(if (showLogs) 0.55f else 1f)
                    .semantics { contentDescription = "Agent Steps List size ${steps.size}" },
                state = listState
            ) {
                items(steps) { step -> AgentStepCard(step) }
            }
            if (showLogs) {
                Spacer(Modifier.width(8.dp))
                LazyColumn(
                    Modifier
                        .weight(0.45f)
                        .semantics { contentDescription = "Agent Log List size ${agentLogs.size}" },
                    state = logListState
                ) {
                    items(agentLogs.take(120)) { log ->
                        Text(
                            text = "${log.timestamp}: ${log.content}",
                            color = Color(0xFF888F99),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (showCancelConfirm) {
        AlertDialog(
            onDismissRequest = { showCancelConfirm = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.cancel();
                    showCancelConfirm = false
                    snackbarScope.launch { SnackbarEvents.dispatch("Agent stopped") }
                }) { Text("Stop", color = Color(0xFFFF5555)) }
            },
            dismissButton = { TextButton(onClick = { showCancelConfirm = false }) { Text("Keep Running") } },
            title = { Text("Cancel Agent Run?") },
            text = { Text("Stopping now will finalize partial results. Continue?") }
        )
    }
}

@Composable
private fun AgentStepCard(step: AgentStep) {
    val statusColor = when (step.status) {
        StepStatus.PENDING -> Color(0xFF6C7A89)
        StepStatus.RUNNING -> Color(0xFFFFA500)
        StepStatus.DONE -> Color(0xFF39FF14)
        StepStatus.FAILED -> Color(0xFFFF3860)
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E252D)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .semantics { contentDescription = "Agent step ${step.description} status ${step.status.name}" }
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(step.description, color = statusColor, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                Text(step.status.name, color = statusColor, style = MaterialTheme.typography.labelSmall)
            }
            if (step.output.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(step.output, color = Color(0xFFB5C6D6), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
