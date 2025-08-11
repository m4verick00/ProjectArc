package com.arclogbook.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import com.arclogbook.ui.components.SnackbarEvents
import com.arclogbook.ui.components.SnackbarMessage
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Logbook : BottomNavItem("logbook", "Logbook", Icons.Filled.Book)
    object Osint : BottomNavItem("osint", "OSINT", Icons.Filled.BugReport)
    object Recon : BottomNavItem("recon", "Recon", Icons.Filled.BugReport)
    object DeviceScan : BottomNavItem("device_scan", "Device Scan", Icons.Filled.BugReport)
    object SocialScan : BottomNavItem("social_scan", "Social Scan", Icons.Filled.BugReport)
    object VulnPredictor : BottomNavItem("vuln_predictor", "AI Vulns", Icons.Filled.BugReport)
    object Phishing : BottomNavItem("phishing", "Phishing", Icons.Filled.BugReport)
    object Agent : BottomNavItem("agent", "Agent", Icons.Filled.BugReport)
    object MigrationTester : BottomNavItem("migration_tester", "PQC Test", Icons.Filled.BugReport)
    object DeepWeb : BottomNavItem("deepweb", "Deep Web", Icons.Filled.DarkMode)
    object Pastebin : BottomNavItem("pastebin", "Pastebin", Icons.Filled.Description)
    object Disclaimer : BottomNavItem("disclaimer", "Disclaimer", Icons.Filled.Security)
    object Settings : BottomNavItem("settings", "Settings", Icons.Filled.Security)
    object RedTeam : BottomNavItem("redteam", "Red Team", Icons.Filled.BugReport)
    object Audit : BottomNavItem("audittrail", "Audit", Icons.Filled.Security)
    object Zkp : BottomNavItem("zkp", "ZK Proof", Icons.Filled.Security)
    object ArVis : BottomNavItem("arvis", "AR", Icons.Filled.BugReport)
    object WearSync : BottomNavItem("wearsync", "Sync", Icons.Filled.Security)
}

val bottomNavItems = listOf(
    BottomNavItem.Logbook,
    BottomNavItem.Osint,
    BottomNavItem.Recon,
    BottomNavItem.DeviceScan,
    BottomNavItem.SocialScan,
    BottomNavItem.VulnPredictor,
    BottomNavItem.Phishing,
    BottomNavItem.Agent,
    BottomNavItem.MigrationTester,
    BottomNavItem.DeepWeb,
    BottomNavItem.Pastebin,
    BottomNavItem.Disclaimer,
    BottomNavItem.Settings
    , BottomNavItem.RedTeam
    , BottomNavItem.Audit
    , BottomNavItem.Zkp
    , BottomNavItem.ArVis
    , BottomNavItem.WearSync
)

@Composable
fun MainScaffold(navController: NavHostController, content: @Composable () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val title = when (currentRoute) {
        "logbook" -> "Logbook"
        "osint" -> "OSINT Toolkit"
        "recon" -> "Recon"
        "device_scan" -> "Device Scan"
        "social_scan" -> "Social Scan"
        "vuln_predictor" -> "Vulnerability Predictor"
        "phishing" -> "Phishing"
        "agent" -> "Agent"
        "migration_tester" -> "PQC Migration"
        "deepweb" -> "Deep Web"
        "pastebin" -> "Pastebin"
        "disclaimer" -> "Disclaimer"
        "settings" -> "Settings"
    "redteam" -> "Red Team Lab"
        else -> "Project Arc"
    }
    LaunchedEffect(Unit) {
        // Collect snackbar messages
        SnackbarEvents.messages.collectLatest { sm: SnackbarMessage ->
            val visual = when (sm.kind) {
                com.arclogbook.ui.components.Kind.Success -> "✅ ${'$'}{sm.text}"
                com.arclogbook.ui.components.Kind.Warning -> "⚠️ ${'$'}{sm.text}"
                com.arclogbook.ui.components.Kind.Error -> "⛔ ${'$'}{sm.text}"
                else -> sm.text
            }
            snackbarHostState.showSnackbar(visual, actionLabel = sm.actionLabel)
        }
    }
    Scaffold(
        topBar = { SmallTopAppBar(title = { Text(title) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            if (currentDestination?.route != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(Modifier.fillMaxSize().padding(innerPadding)) { content() }
        }
    )
}
