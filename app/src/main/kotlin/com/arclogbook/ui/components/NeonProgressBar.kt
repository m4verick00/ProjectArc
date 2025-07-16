package com.arclogbook.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NeonProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Int = 8,
    color: Color = Color(0xFF00FFFF)
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    Box(
        modifier = modifier
            .height(height.dp)
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.3f),
                        color.copy(alpha = 0.7f),
                        color
                    )
                )
            )
            .shadow(4.dp, ambientColor = color, spotColor = color)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            color,
                            Color.Magenta,
                            Color.Cyan
                        )
                    )
                )
        )
    }
}
