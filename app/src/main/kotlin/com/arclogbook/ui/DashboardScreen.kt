package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arclogbook.viewmodel.LogbookViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.LottieLoading
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@Composable
fun DashboardScreen(viewModel: LogbookViewModel = hiltViewModel()) {
    val logEntries = viewModel.logEntries.collectAsState().value
    val typeCounts = logEntries.groupBy { it.type }.mapValues { it.value.size }
    val tagCounts = logEntries.flatMap { it.tags.split(",") }.groupingBy { it.trim() }.eachCount().filter { it.key.isNotBlank() }
    val recent = logEntries.sortedByDescending { it.timestamp }.take(5)
    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = Brush.linearGradient(listOf(Color(0xFF23272E), Color(0xFF181A20), Color(0xFF00FFFF).copy(alpha = 0.15f)))
    if (logEntries.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LottieLoading()
            Text("No data yet!", style = MaterialTheme.typography.titleMedium, color = Color.Gray, modifier = Modifier.align(Alignment.BottomCenter).padding(top = 160.dp))
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            Text("Dashboard", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            MotionLayout(
                motionScene = MotionScene("""
                {
                  ConstraintSets: {
                    start: { card: { width: 'spread', height: 120, translationZ: 0 } },
                    end: { card: { width: 'spread', height: 220, translationZ: 16 } }
                  },
                  Transitions: {
                    default: { from: 'start', to: 'end', pathMotionArc: 'startVertical', duration: 400 }
                  }
                }
                """),
                progress = 1f,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    Modifier
                        .layoutId("card")
                        .fillMaxWidth()
                        .shadow(16.dp, cardShape)
                        .clip(cardShape)
                        .background(cardGradient),
                    shape = cardShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Entries by Type: $typeCounts", style = MaterialTheme.typography.labelMedium)
                        Text("Tags: $tagCounts", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Recent Activity:", style = MaterialTheme.typography.titleMedium)
            recent.forEach {
                Text("- [${it.type}] ${it.tags} (${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(it.timestamp))})")
            }
            // TODO: Add charts/graphs for trends
        }
    }
}
