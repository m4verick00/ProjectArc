package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.theme.ArcThemeType
import com.arclogbook.viewmodel.OneDriveSyncViewModel

@Composable
fun SettingsScreen(
    currentTheme: ArcThemeType,
    onThemeChange: (ArcThemeType) -> Unit,
    syncViewModel: OneDriveSyncViewModel = hiltViewModel()
) {
    var showBiometric by remember { mutableStateOf(false) }
    var syncStatus by syncViewModel.syncStatus.collectAsState()
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        Divider()
        Text("Theme", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            ArcThemeType.values().forEach { theme ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                    RadioButton(selected = currentTheme == theme, onClick = { onThemeChange(theme) })
                    Text(theme.name)
                }
            }
        }
        Divider()
        Button(onClick = { showBiometric = true }) {
            Text("Test Biometric Unlock")
        }
        Divider()
        Button(onClick = { syncViewModel.authenticate({}, {}) }) {
            Text("Sign in to OneDrive")
        }
        Button(onClick = { syncViewModel.uploadBackup(/*file*/null, "All") }) {
            Text("Backup Now")
        }
        Button(onClick = { syncViewModel.downloadBackup(/*file*/null) }) {
            Text("Restore Backup")
        }
        Text(syncStatus, style = MaterialTheme.typography.labelSmall)
    }
    if (showBiometric) {
        BiometricUnlockScreen(onUnlock = { showBiometric = false }, onError = { showBiometric = false })
    }
}
