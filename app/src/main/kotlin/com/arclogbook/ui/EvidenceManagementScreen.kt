package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EvidenceManagementScreen(
    onAddEvidence: (String) -> Unit
) {
    var evidence by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = evidence,
            onValueChange = { evidence = it },
            label = { Text("Evidence Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onAddEvidence(evidence) }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Evidence")
        }
    }
}
