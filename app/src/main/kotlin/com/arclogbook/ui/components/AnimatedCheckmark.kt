package com.arclogbook.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCheckmark(
    checked: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF00FFFF),
    size: Int = 32
) {
    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    Canvas(modifier = modifier.size(size.dp)) {
        // Draw circle
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = size.dp.toPx() / 2,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
        )
        // Draw checkmark
        if (progress > 0f) {
            val startX = size.dp.toPx() * 0.3f
            val startY = size.dp.toPx() * 0.55f
            val midX = size.dp.toPx() * 0.45f
            val midY = size.dp.toPx() * 0.7f
            val endX = size.dp.toPx() * 0.75f
            val endY = size.dp.toPx() * 0.35f
            val pathProgress = progress
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(startX, startY),
                end = androidx.compose.ui.geometry.Offset(midX, midY),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round,
                alpha = pathProgress
            )
            if (progress > 0.5f) {
                val secondProgress = (progress - 0.5f) * 2f
                drawLine(
                    color = color,
                    start = androidx.compose.ui.geometry.Offset(midX, midY),
                    end = androidx.compose.ui.geometry.Offset(
                        midX + (endX - midX) * secondProgress,
                        midY - (midY - endY) * secondProgress
                    ),
                    strokeWidth = 5.dp.toPx(),
                    cap = StrokeCap.Round,
                    alpha = secondProgress
                )
            }
        }
    }
}
