package com.arclogbook.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HighContrastColorScheme = darkColorScheme(
    primary = Color(0xFF00FFFF),
    onPrimary = Color.Black,
    background = Color.Black,
    onBackground = Color(0xFF00FFFF),
    surface = Color(0xFF222222),
    onSurface = Color(0xFF00FFFF)
)

@Composable
fun HighContrastTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HighContrastColorScheme,
        typography = Typography(),
        content = content
    )
}
