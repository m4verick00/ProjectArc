package com.arclogbook.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun VisualWorkflowBuilderScreen(onBuild: (List<String>) -> Unit) {
    val steps = remember { mutableStateOf(listOf<String>()) }
    Text("Visual Workflow Builder (Drag-and-Drop UI coming soon)")
    Button(onClick = { onBuild(steps.value) }) { Text("Build Workflow") }
}
