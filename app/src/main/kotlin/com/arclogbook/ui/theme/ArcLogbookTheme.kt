package com.arclogbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberpunkColorScheme = darkColorScheme(
    primary = Color(0xFF39FF14), // Neon green
    secondary = Color(0xFF00FFFF), // Neon cyan
    tertiary = Color(0xFFFF00FF), // Neon magenta
    background = Color(0xFF181A20),
    surface = Color(0xFF23272E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFF39FF14),
    onSurface = Color(0xFF00FFFF)
)

private val MinimalisticColorScheme = darkColorScheme(
    primary = Color(0xFFB0BEC5),
    secondary = Color(0xFF90A4AE),
    tertiary = Color(0xFF78909C),
    background = Color(0xFF212121),
    surface = Color(0xFF263238),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val ClassicColorScheme = darkColorScheme(
    primary = Color(0xFF607D8B),
    secondary = Color(0xFF795548),
    tertiary = Color(0xFF9E9E9E),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

enum class ArcThemeType { CYBERPUNK, MINIMALISTIC, CLASSIC }

@Composable
fun ArcLogbookTheme(
    themeType: ArcThemeType = ArcThemeType.CYBERPUNK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        ArcThemeType.CYBERPUNK -> CyberpunkColorScheme
        ArcThemeType.MINIMALISTIC -> MinimalisticColorScheme
        ArcThemeType.CLASSIC -> ClassicColorScheme
    }
    val typography = when (themeType) {
        ArcThemeType.CYBERPUNK -> cyberpunkTypography
        ArcThemeType.MINIMALISTIC -> minimalisticTypography
        ArcThemeType.CLASSIC -> classicTypography
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
