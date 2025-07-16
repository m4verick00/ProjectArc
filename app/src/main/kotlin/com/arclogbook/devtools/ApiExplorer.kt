package com.arclogbook.devtools

import androidx.compose.runtime.Composable
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ApiExplorer(onTest: (String) -> Unit) {
    val endpoint = remember { mutableStateOf("") }
    Text("API Explorer")
    TextField(value = endpoint.value, onValueChange = { endpoint.value = it }, label = { Text("Endpoint") })
    Button(onClick = { onTest(endpoint.value) }) { Text("Test") }
}
