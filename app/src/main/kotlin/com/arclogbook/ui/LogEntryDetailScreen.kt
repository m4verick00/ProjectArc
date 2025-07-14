package com.arclogbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arclogbook.data.LogEntry
import com.arclogbook.ui.LottieLoading
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@Composable
fun LogEntryDetailScreen(entry: LogEntry, onExportJson: (() -> Unit)? = null) {
    val clipboardManager = LocalClipboardManager.current
    var isStarred by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF39FF14).copy(alpha = 0.15f)))
    Column(
        Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(entry.type, color = Color(0xFF39FF14), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { isStarred = !isStarred }) {
                Icon(if (isStarred) Icons.Default.Star else Icons.Default.StarBorder, contentDescription = "Star")
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Tags: ", style = MaterialTheme.typography.labelMedium, color = Color.Cyan)
        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 4.dp) {
            entry.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { tag ->
                AssistChip(onClick = {}, label = { Text(tag) })
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Timestamp: " + java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(entry.timestamp)), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Content:", style = MaterialTheme.typography.titleMedium, color = Color(0xFF00FFFF))
        if (entry.content.isBlank()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LottieLoading()
                Text("No content!", style = MaterialTheme.typography.titleMedium, color = Color.Gray, modifier = Modifier.align(Alignment.BottomCenter).padding(top = 160.dp))
            }
        } else {
            MotionLayout(
                motionScene = MotionScene("""
                {
                  ConstraintSets: {
                    collapsed: { card: { width: 'spread', height: 120, translationZ: 0 } },
                    expanded: { card: { width: 'spread', height: 320, translationZ: 16 } }
                  },
                  Transitions: {
                    default: { from: 'collapsed', to: 'expanded', pathMotionArc: 'startVertical', duration: 400 }
                  }
                }
                """),
                progress = 1f,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .layoutId("card")
                        .fillMaxWidth()
                        .shadow(16.dp, cardShape)
                        .clip(cardShape)
                        .background(cardGradient),
                    color = Color.Transparent
                ) {
                    Column(
                        Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(entry.content, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.padding(12.dp))
                        Spacer(Modifier.height(16.dp))
                        Row {
                            Button(onClick = { clipboardManager.setText(AnnotatedString(entry.content)) }, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                Spacer(Modifier.width(4.dp))
                                Text("Copy")
                            }
                            Spacer(Modifier.width(8.dp))
                            if (onExportJson != null) {
                                Button(onClick = onExportJson, modifier = Modifier.weight(1f)) {
                                    Text("Export as JSON")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
