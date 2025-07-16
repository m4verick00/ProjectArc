package com.arclogbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arclogbook.llm.LLMManager
import com.arclogbook.ui.components.CyberpunkButton

@Composable
fun ChatbotScreen(onAutomateTask: (String) -> Unit) {
    val context = LocalContext.current
    val llmManager = remember { LLMManager(context) }
    var chatHistory by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var userInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF181A20), Color(0xFF23272E), Color(0xFF00FFFF).copy(alpha = 0.15f))
                )
            )
            .padding(16.dp)
    ) {
        Text("ArcLogbook Chatbot", style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chatHistory.size) { idx ->
                val (user, bot) = chatHistory[idx]
                Text("You: $user", color = Color.Cyan)
                Text("Bot: $bot", color = Color(0xFF39FF14))
                Spacer(Modifier.height(4.dp))
            }
        }
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Type a command or question...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                isLoading = true
                val response = llmManager.runInference(userInput)
                chatHistory = chatHistory + (userInput to response)
                onAutomateTask(userInput)
                userInput = ""
                isLoading = false
            })
        )
        CyberpunkButton(
            onClick = {
                isLoading = true
                val response = llmManager.runInference(userInput)
                chatHistory = chatHistory + (userInput to response)
                onAutomateTask(userInput)
                userInput = ""
                isLoading = false
            },
            text = if (isLoading) "Thinking..." else "Send",
            color = MaterialTheme.colorScheme.primary
        )
    }
}
