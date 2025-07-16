package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CaseTrackingScreen(
    onTrackCase: (String) -> Unit
) {
    var caseName by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = caseName,
            onValueChange = { caseName = it },
            label = { Text("Case Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onTrackCase(caseName) }, modifier = Modifier.fillMaxWidth()) {
            Text("Track Case")
        }
    }
}
