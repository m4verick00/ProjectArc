package com.arclogbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.arclogbook.ui.theme.cyberpunkGradient

@Composable
fun AdvancedSearchScreen(
    onSearch: (String, String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val fontScale = LocalContext.current.resources.configuration.fontScale
    val scaledTitleSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale).sp
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF181A20), Color(0xFF23272E), Color(0xFF00FFFF).copy(alpha = 0.15f))
                )
            )
            .animateContentSize()
    ) {
        Text(
            "Advanced Search",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = scaledTitleSize, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = query,
            onValueChange = {
                query = it
                showError = false
            },
            label = { Text("Search Query") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && query.isBlank(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tag,
            onValueChange = {
                tag = it
                showError = false
            },
            label = { Text("Tag") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && tag.isBlank(),
            singleLine = true
        )
        if (showError) {
            Text(errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))
        CyberpunkButton(
            onClick = {
                if (query.isBlank() || tag.isBlank()) {
                    showError = true
                    errorMessage = "Both fields are required."
                } else {
                    onSearch(query, tag)
                }
            },
            text = "Search",
            color = MaterialTheme.colorScheme.primary
        )
    }
}
