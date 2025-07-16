package com.arclogbook.devtools

import androidx.compose.runtime.Composable
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun InAppCodeEditor(onSave: (String) -> Unit) {
    val code = remember { mutableStateOf("") }
    Text("In-App Code Editor")
    TextField(value = code.value, onValueChange = { code.value = it }, label = { Text("Code") })
    Button(onClick = { onSave(code.value) }) { Text("Save") }
}
