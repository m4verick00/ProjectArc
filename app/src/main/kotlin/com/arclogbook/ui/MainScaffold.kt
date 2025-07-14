package com.arclogbook.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Logbook : BottomNavItem("logbook", "Logbook", Icons.Filled.Book)
    object Osint : BottomNavItem("osint", "OSINT", Icons.Filled.BugReport)
    object DeepWeb : BottomNavItem("deepweb", "Deep Web", Icons.Filled.DarkMode)
    object Pastebin : BottomNavItem("pastebin", "Pastebin", Icons.Filled.Description)
    object Disclaimer : BottomNavItem("disclaimer", "Disclaimer", Icons.Filled.Security)
    object Settings : BottomNavItem("settings", "Settings", Icons.Filled.Security)
}

val bottomNavItems = listOf(
    BottomNavItem.Logbook,
    BottomNavItem.Osint,
    BottomNavItem.DeepWeb,
    BottomNavItem.Pastebin,
    BottomNavItem.Disclaimer,
    BottomNavItem.Settings
)

@Composable
fun MainScaffold(navController: NavHostController, content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
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
            Surface(modifier = androidx.compose.ui.Modifier.padding(innerPadding)) {
                content()
            }
        }
    )
}
