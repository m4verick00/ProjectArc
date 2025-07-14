package com.arclogbook.ui

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arclogbook.ui.theme.ArcThemeType

@Composable
fun ArcLogbookNavGraph(
    navController: NavHostController = rememberNavController(),
    currentTheme: ArcThemeType = ArcThemeType.CYBERPUNK,
    onThemeChange: (ArcThemeType) -> Unit = {},
    onUnlock: () -> Unit = {}
) {
    NavHost(navController = navController, startDestination = "biometric_unlock") {
        composable("biometric_unlock") {
            BiometricUnlockScreen(
                onUnlock = { navController.navigate("logbook") },
                onError = { /* Handle error, maybe show a dialog */ }
            )
        }
        composable("logbook") {
            LogbookScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(currentTheme = currentTheme, onThemeChange = onThemeChange)
        }
        composable("osint") { OsintToolkitScreen() }
        composable("deepweb") { DeepWebMonitorScreen() }
        composable("pastebin") { PastebinMonitorScreen() }
        composable("disclaimer") { ResearchDisclaimerScreen() }
    }
}
