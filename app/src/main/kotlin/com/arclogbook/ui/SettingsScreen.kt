package com.arclogbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arclogbook.ui.theme.ArcThemeType
import com.arclogbook.viewmodel.OneDriveSyncViewModel
import com.arclogbook.ui.components.AnimatedCheckmark
import com.arclogbook.security.DeviceSecurityUtils
import com.arclogbook.security.SecurityLogger
import com.arclogbook.security.SignatureUtils
import com.arclogbook.security.VpnTorUtils
import com.arclogbook.security.EncryptedPrefsUtils
import com.arclogbook.ui.HighContrastTheme
import com.arclogbook.security.GlobalErrorLogger
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.arclogbook.security.CrashReporter
import com.arclogbook.security.SecurityAuditDashboard
import com.arclogbook.security.PrivacyScanner
import com.arclogbook.security.DataWipeUtils
import com.arclogbook.security.StealthModeUtils
import com.arclogbook.security.BiometricAuthUtils
import com.arclogbook.security.AppIntegrityUtils
import com.arclogbook.security.ThreatDetectionUtils
import com.arclogbook.security.RemoteWipeUtils
import com.arclogbook.ui.ThemeEditorScreen
import com.arclogbook.ui.AccessibilityWizardScreen
import com.arclogbook.plugin.PluginMarketplace
import com.arclogbook.analytics.LoggingAnalyticsDashboard
import androidx.fragment.app.FragmentActivity

@Composable
fun SettingsScreen(
    currentTheme: ArcThemeType,
    onThemeChange: (ArcThemeType) -> Unit,
    currentFont: String,
    onFontChange: (String) -> Unit,
    syncViewModel: OneDriveSyncViewModel = hiltViewModel()
) {
    var showBiometric by remember { mutableStateOf(false) }
    var syncStatus by syncViewModel.syncStatus.collectAsState()
    var backupCompleted by remember { mutableStateOf(false) }
    var restoreCompleted by remember { mutableStateOf(false) }
    val fontOptions = listOf("Orbitron", "Exo", "Audiowide", "Rajdhani", "SF Pro")
    val showTorDialog = remember { mutableStateOf(false) }
    val showPrefsDialog = remember { mutableStateOf(false) }
    val showContrastDialog = remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    var showErrorLogDialog by remember { mutableStateOf(false) }
    var showCrashLogDialog by remember { mutableStateOf(false) }
    var showSecurityAuditDialog by remember { mutableStateOf(false) }
    var showPrivacyScanDialog by remember { mutableStateOf(false) }
    var showWipeDialog by remember { mutableStateOf(false) }
    var showStealthDialog by remember { mutableStateOf(false) }
    var showBiometricDialog by remember { mutableStateOf(false) }
    var showIntegrityDialog by remember { mutableStateOf(false) }
    var showThreatDialog by remember { mutableStateOf(false) }
    var showRemoteWipeDialog by remember { mutableStateOf(false) }
    var showThemeEditor by remember { mutableStateOf(false) }
    var showAccessibilityWizard by remember { mutableStateOf(false) }
    var showPluginMarketplace by remember { mutableStateOf(false) }
    var showAnalyticsDashboard by remember { mutableStateOf(false) }
    var privacyScanResult by remember { mutableStateOf("") }
    var integrityResult by remember { mutableStateOf("") }
    var threatResult by remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(Color(0xFF181A20), Color(0xFF23272E), Color(0xFF00FFFF).copy(alpha = 0.15f)))
            )
            .padding(24.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Divider()
        Text("Theme", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            ArcThemeType.values().forEach { theme ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = { onThemeChange(theme) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(theme.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Text("Font", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf("Orbitron", "Exo", "Audiowide", "Rajdhani", "SF Pro").forEach { font ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                    RadioButton(
                        selected = currentFont == font,
                        onClick = { onFontChange(font) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text(font, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        CyberpunkButton(onClick = { showErrorLogDialog = true }, text = "Show Error Logs", color = MaterialTheme.colorScheme.primary)
        CyberpunkButton(onClick = { showCrashLogDialog = true }, text = "Show Crash Logs", color = MaterialTheme.colorScheme.secondary)
        CyberpunkButton(onClick = { showSecurityAuditDialog = true }, text = "Security Audit", color = MaterialTheme.colorScheme.tertiary)
        CyberpunkButton(onClick = { showPrivacyScanDialog = true }, text = "Privacy Scan", color = MaterialTheme.colorScheme.primary)
        CyberpunkButton(onClick = { showWipeDialog = true }, text = "Data Wipe", color = MaterialTheme.colorScheme.secondary)
        CyberpunkButton(onClick = { showStealthDialog = true }, text = "Stealth Mode", color = MaterialTheme.colorScheme.tertiary)
        CyberpunkButton(onClick = { showBiometricDialog = true }, text = "Biometric Auth", color = MaterialTheme.colorScheme.primary)
        CyberpunkButton(onClick = { showIntegrityDialog = true }, text = "App Integrity", color = MaterialTheme.colorScheme.secondary)
        CyberpunkButton(onClick = { showThreatDialog = true }, text = "Threat Detection", color = MaterialTheme.colorScheme.tertiary)
        CyberpunkButton(onClick = { showRemoteWipeDialog = true }, text = "Remote Wipe", color = MaterialTheme.colorScheme.primary)
        CyberpunkButton(onClick = { showThemeEditor = true }, text = "Theme Editor", color = MaterialTheme.colorScheme.secondary)
        CyberpunkButton(onClick = { showAccessibilityWizard = true }, text = "Accessibility Wizard", color = MaterialTheme.colorScheme.tertiary)
        CyberpunkButton(onClick = { showPluginMarketplace = true }, text = "Plugin Marketplace", color = MaterialTheme.colorScheme.primary)
        CyberpunkButton(onClick = { showAnalyticsDashboard = true }, text = "Analytics Dashboard", color = MaterialTheme.colorScheme.secondary)
        Text("OSINT API Keys & Addresses", style = MaterialTheme.typography.labelLarge)
        var selectedService by remember { mutableStateOf("") }
        var apiKeyInput by remember { mutableStateOf("") }
        val serviceOptions = listOf(
            "VirusTotal", "Shodan", "HaveIBeenPwned", "Censys", "AbuseIPDB", "Pastebin"
        )
        DropdownMenu(
            expanded = true,
            onDismissRequest = {},
            modifier = Modifier.fillMaxWidth().background(Color(0xFF23272E)),
        ) {
            serviceOptions.forEach { service ->
                DropdownMenuItem(
                    text = { Text(service) },
                    onClick = { selectedService = service }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = apiKeyInput,
            onValueChange = { apiKeyInput = it },
            label = { Text("Enter API Key / Address for $selectedService") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00FFFF),
                unfocusedBorderColor = Color(0xFF23272E),
                textColor = Color(0xFF00FFFF)
            )
        )
        CyberpunkButton(
            onClick = {
                EncryptedPrefsUtils.saveApiKey(selectedService, apiKeyInput)
            },
            text = "Save API Key / Address",
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
    }
    if (showTorDialog.value) {
        AlertDialog(
            onDismissRequest = { showTorDialog.value = false },
            title = { Text("Enable Tor") },
            text = { Text("Launch Orbot for Tor network integration?") },
            confirmButton = {
                Button(onClick = {
                    VpnTorUtils.launchOrbot(LocalContext.current)
                    showTorDialog.value = false
                }) { Text("Launch") }
            },
            dismissButton = {
                Button(onClick = { showTorDialog.value = false }) { Text("Cancel") }
            }
        )
    }
    if (showPrefsDialog.value) {
        AlertDialog(
            onDismissRequest = { showPrefsDialog.value = false },
            title = { Text("Encrypted Preferences") },
            text = { Text("Access encrypted preferences?") },
            confirmButton = {
                Button(onClick = {
                    val prefs = EncryptedPrefsUtils.getEncryptedPrefs(LocalContext.current)
                    showPrefsDialog.value = false
                }) { Text("Access") }
            },
            dismissButton = {
                Button(onClick = { showPrefsDialog.value = false }) { Text("Cancel") }
            }
        )
    }
    if (showContrastDialog.value) {
        AlertDialog(
            onDismissRequest = { showContrastDialog.value = false },
            title = { Text("High Contrast Mode") },
            text = { Text("Enable high-contrast accessibility mode?") },
            confirmButton = {
                Button(onClick = {
                    HighContrastTheme { /* Recompose UI with high contrast */ }
                    showContrastDialog.value = false
                }) { Text("Enable") }
            },
            dismissButton = {
                Button(onClick = { showContrastDialog.value = false }) { Text("Cancel") }
            }
        )
    }
    if (showErrorLogDialog) {
        val errorLog = GlobalErrorLogger.getAllErrors().joinToString("\n\n")
        AlertDialog(
            onDismissRequest = { showErrorLogDialog = false },
            title = { Text("Error Log") },
            text = { Text(errorLog.ifBlank { "No errors logged." }) },
            confirmButton = {
                Button(onClick = {
                    clipboardManager.setText(AnnotatedString(errorLog))
                    showErrorLogDialog = false
                }) { Text("Copy to Clipboard") }
            },
            dismissButton = {
                Button(onClick = { showErrorLogDialog = false }) { Text("Close") }
            }
        )
    }
    if (showCrashLogDialog) {
        val crashLog = CrashReporter.getAllCrashes().joinToString("\n\n")
        AlertDialog(
            onDismissRequest = { showCrashLogDialog = false },
            title = { Text("Crash Log") },
            text = { Text(crashLog.ifBlank { "No crashes logged." }) },
            confirmButton = {
                Button(onClick = {
                    clipboardManager.setText(AnnotatedString(crashLog))
                    showCrashLogDialog = false
                }) { Text("Copy to Clipboard") }
            },
            dismissButton = {
                Button(onClick = { showCrashLogDialog = false }) { Text("Close") }
            }
        )
    }
    if (showSecurityAuditDialog) {
        val auditLog = SecurityAuditDashboard.getEvents().joinToString("\n\n")
        AlertDialog(
            onDismissRequest = { showSecurityAuditDialog = false },
            title = { Text("Security Audit Dashboard") },
            text = { Text(auditLog.ifBlank { "No security events logged." }) },
            confirmButton = {
                Button(onClick = {
                    clipboardManager.setText(AnnotatedString(auditLog))
                    showSecurityAuditDialog = false
                }) { Text("Copy to Clipboard") }
            },
            dismissButton = {
                Button(onClick = { showSecurityAuditDialog = false }) { Text("Close") }
            }
        )
    }
    if (showPrivacyScanDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyScanDialog = false },
            title = { Text("Privacy Scanner") },
            text = {
                Column {
                    Text("Scan for device identifiers in app data/code.")
                    Text(privacyScanResult)
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Example scan
                    privacyScanResult = PrivacyScanner.scanForIdentifiers("AppData IMEI MAC Android ID").joinToString()
                    showPrivacyScanDialog = false
                }) { Text("Run Scan") }
            },
            dismissButton = {
                Button(onClick = { showPrivacyScanDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showWipeDialog) {
        AlertDialog(
            onDismissRequest = { showWipeDialog = false },
            title = { Text("Wipe All Data") },
            text = { Text("Are you sure you want to securely erase all app data?") },
            confirmButton = {
                Button(onClick = {
                    DataWipeUtils.wipeAppData(LocalContext.current)
                    showWipeDialog = false
                }) { Text("Wipe Data") }
            },
            dismissButton = {
                Button(onClick = { showWipeDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showStealthDialog) {
        AlertDialog(
            onDismissRequest = { showStealthDialog = false },
            title = { Text("Stealth Mode") },
            text = { Text("Hide or show app icon. Choose action:") },
            confirmButton = {
                Button(onClick = {
                    StealthModeUtils.hideAppIcon(LocalContext.current)
                    showStealthDialog = false
                }) { Text("Hide Icon") }
            },
            dismissButton = {
                Button(onClick = {
                    StealthModeUtils.showAppIcon(LocalContext.current)
                    showStealthDialog = false
                }) { Text("Show Icon") }
            }
        )
    }
    if (showBiometricDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricDialog = false },
            title = { Text("Biometric Authentication") },
            text = { Text("Authenticate to proceed.") },
            confirmButton = {
                Button(onClick = {
                    // Requires FragmentActivity context
                    // BiometricAuthUtils.authenticate(activity, {...}, {...})
                    showBiometricDialog = false
                }) { Text("Authenticate") }
            },
            dismissButton = {
                Button(onClick = { showBiometricDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showIntegrityDialog) {
        AlertDialog(
            onDismissRequest = { showIntegrityDialog = false },
            title = { Text("App Integrity Attestation") },
            text = { Text(integrityResult.ifBlank { "Check app integrity?" }) },
            confirmButton = {
                Button(onClick = {
                    AppIntegrityUtils.attest(LocalContext.current) { result -> integrityResult = if (result) "Integrity OK" else "Integrity Failed" }
                    showIntegrityDialog = false
                }) { Text("Check") }
            },
            dismissButton = {
                Button(onClick = { showIntegrityDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showThreatDialog) {
        AlertDialog(
            onDismissRequest = { showThreatDialog = false },
            title = { Text("Threat Detection") },
            text = { Text(threatResult.ifBlank { "Detect suspicious activity?" }) },
            confirmButton = {
                Button(onClick = {
                    val suspicious = ThreatDetectionUtils.isSuspiciousNetwork(LocalContext.current)
                    val tampered = ThreatDetectionUtils.isAppTampered()
                    threatResult = "Network: ${if (suspicious) "Suspicious" else "OK"}, App: ${if (tampered) "Tampered" else "OK"}"
                    showThreatDialog = false
                }) { Text("Detect") }
            },
            dismissButton = {
                Button(onClick = { showThreatDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showRemoteWipeDialog) {
        AlertDialog(
            onDismissRequest = { showRemoteWipeDialog = false },
            title = { Text("Remote Wipe") },
            text = { Text("Enter command to wipe app remotely.") },
            confirmButton = {
                Button(onClick = {
                    RemoteWipeUtils.remoteWipe(LocalContext.current, "WIPE_ARC")
                    showRemoteWipeDialog = false
                }) { Text("Wipe") }
            },
            dismissButton = {
                Button(onClick = { showRemoteWipeDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showThemeEditor) {
        ThemeEditorScreen()
    }
    if (showAccessibilityWizard) {
        AccessibilityWizardScreen()
    }
    if (showPluginMarketplace) {
        val plugins = PluginMarketplace.discoverPlugins().joinToString()
        AlertDialog(
            onDismissRequest = { showPluginMarketplace = false },
            title = { Text("Plugin Marketplace") },
            text = { Text(plugins) },
            confirmButton = {
                Button(onClick = { showPluginMarketplace = false }) { Text("Close") }
            },
            dismissButton = null
        )
    }
    if (showAnalyticsDashboard) {
        val logs = LoggingAnalyticsDashboard.getLogs().joinToString("\n")
        AlertDialog(
            onDismissRequest = { showAnalyticsDashboard = false },
            title = { Text("Analytics Dashboard") },
            text = { Text(logs.ifBlank { "No logs yet." }) },
            confirmButton = {
                Button(onClick = { showAnalyticsDashboard = false }) { Text("Close") }
            },
            dismissButton = null
        )
    }
    if (showBiometric) {
        BiometricUnlockScreen(onUnlock = { showBiometric = false }, onError = { showBiometric = false })
    }
    LaunchedEffect(Unit) {
        if (DeviceSecurityUtils.isEmulator()) {
            SecurityLogger.logSuspiciousActivity("App running on emulator")
        }
        if (DeviceSecurityUtils.isRooted()) {
            SecurityLogger.logSuspiciousActivity("Device is rooted")
        }
        val expectedSignature = "YOUR_BASE64_SIGNATURE" // Replace with your actual signature
        if (!SignatureUtils.isSignatureValid(context, expectedSignature)) {
            SecurityLogger.logSuspiciousActivity("App signature mismatch (possible tampering)")
        }
    }
}
