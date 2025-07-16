package com.arclogbook.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.arclogbook.ui.theme.ArcLogbookTheme
import com.arclogbook.ui.theme.ArcThemeType

@Composable
fun ArcLogbookApp() {
    var themeType by remember { mutableStateOf(ArcThemeType.CYBERPUNK) }
    var selectedFont by remember { mutableStateOf("Orbitron") }
    ArcLogbookTheme(themeType = themeType, font = selectedFont) {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            MainScaffold(navController = navController) {
                ArcLogbookNavGraph(
                    navController = navController,
                    currentTheme = themeType,
                    onThemeChange = { themeType = it },
                    currentFont = selectedFont,
                    onFontChange = { selectedFont = it }
                )
            }
        }
    }
}
