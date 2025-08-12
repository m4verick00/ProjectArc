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
    currentFont: String = "Orbitron",
    onFontChange: (String) -> Unit = {},
    onUnlock: () -> Unit = {}
) {
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }
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
            SettingsScreen(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
                currentFont = currentFont,
                onFontChange = onFontChange
            )
        }
        composable("osint") { OsintToolkitScreen() }
        composable("deepweb") { DeepWebMonitorScreen() }
        composable("pastebin") { PastebinMonitorScreen() }
        composable("disclaimer") { ResearchDisclaimerScreen() }
        composable("advanced_search") { AdvancedSearchScreen(onSearch = { query, tag -> /* Pass to ViewModel */ }) }
        composable("notes") { NoteTakingScreen(onSave = { /* Save note to ViewModel */ }) }
        composable("evidence") { EvidenceManagementScreen(onAddEvidence = { /* Save evidence to ViewModel */ }) }
        composable("cases") { CaseTrackingScreen(onTrackCase = { /* Save case to ViewModel */ }) }
        composable("map") { MapScreen() }
        composable("dashboard") { DashboardScreen() }
        composable("chatbot") {
            ChatbotScreen(context = navController.context)
        }
    }
}
