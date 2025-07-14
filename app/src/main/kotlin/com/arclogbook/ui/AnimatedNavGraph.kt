package com.arclogbook.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedArcLogbookNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "logbook") {
        composable("logbook") {
            AnimatedContent(targetState = "logbook", transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            }) { LogbookScreen() }
        }
        composable("osint") {
            AnimatedContent(targetState = "osint", transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            }) { OsintToolkitScreen() }
        }
        composable("deepweb") {
            AnimatedContent(targetState = "deepweb", transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            }) { DeepWebMonitorScreen() }
        }
        composable("pastebin") {
            AnimatedContent(targetState = "pastebin", transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            }) { PastebinMonitorScreen() }
        }
        composable("disclaimer") {
            AnimatedContent(targetState = "disclaimer", transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            }) { ResearchDisclaimerScreen() }
        }
        composable(
            "logentrydetail/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: 0
            val logbookViewModel: LogbookViewModel = hiltViewModel()
            val entry = logbookViewModel.logEntries.collectAsState().value.find { it.id == entryId }
            if (entry != null) {
                LogEntryDetailScreen(entry = entry, onExportJson = { /* TODO: Export logic */ })
            } else {
                Text("Entry not found", color = Color.Red)
            }
        }
    }
}
