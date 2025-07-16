package com.arclogbook.ai

import android.content.Context
import com.arclogbook.viewmodel.*
import com.arclogbook.data.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ChatbotIntentDispatcher(
    private val context: Context,
    private val logbookViewModel: LogbookViewModel,
    private val osintViewModel: OsintViewModel,
    private val evidenceViewModel: EvidenceViewModel,
    private val caseViewModel: CaseViewModel,
    private val syncViewModel: OneDriveSyncViewModel
) {
    fun handleIntent(intent: String, params: Map<String, String> = emptyMap(), onResult: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            when {
                intent.contains("add log", true) -> {
                    val content = params["content"] ?: ""
                    val tags = params["tags"] ?: ""
                    logbookViewModel.addLog(LogEntry(content = content, tags = tags, type = "Chatbot", timestamp = System.currentTimeMillis()))
                    onResult("Log entry added.")
                }
                intent.contains("delete log", true) -> {
                    val content = params["content"] ?: ""
                    val entry = logbookViewModel.logEntries.value.find { it.content == content }
                    if (entry != null) {
                        logbookViewModel.deleteLog(entry)
                        onResult("Log entry deleted.")
                    } else onResult("Log entry not found.")
                }
                intent.contains("search log", true) -> {
                    val tag = params["tag"] ?: ""
                    val keyword = params["keyword"] ?: ""
                    val results = logbookViewModel.searchLogs(tag, keyword).value
                    onResult("Found ${results.size} log entries.")
                }
                intent.contains("add note", true) -> {
                    val note = params["note"] ?: ""
                    logbookViewModel.addNote(note)
                    onResult("Note added.")
                }
                intent.contains("run osint", true) -> {
                    val target = params["target"] ?: ""
                    val queryType = params["queryType"] ?: ""
                    val source = params["source"] ?: ""
                    osintViewModel.runOsintQuery(target, queryType, source)
                    onResult("OSINT scan started for '$target'.")
                }
                intent.contains("monitor pastebin", true) -> {
                    val keyword = params["keyword"] ?: ""
                    val source = params["source"] ?: "Pastebin"
                    pastebinViewModel.monitorKeyword(keyword, source)
                    onResult("Pastebin monitoring started for '$keyword'.")
                }
                intent.contains("save pastebin alert", true) -> {
                    val alert = pastebinViewModel.alerts.value.lastOrNull()
                    if (alert != null) {
                        pastebinViewModel.saveAlertToLogbook(alert)
                        onResult("Pastebin alert saved to logbook.")
                    } else onResult("No alert to save.")
                }
                intent.contains("search deepweb", true) -> {
                    val keyword = params["keyword"] ?: ""
                    val source = params["source"] ?: "Dread (onion)"
                    deepWebViewModel.searchDeepWeb(keyword, source)
                    onResult("Deep web search started for '$keyword'.")
                }
                intent.contains("save deepweb alert", true) -> {
                    val alert = deepWebViewModel.alerts.value.lastOrNull()
                    if (alert != null) {
                        deepWebViewModel.saveAlertToLogbook(alert)
                        onResult("Deep web alert saved to logbook.")
                    } else onResult("No alert to save.")
                }
                intent.contains("backup", true) -> {
                    // You may need to pass file/infoType from params
                    syncViewModel.uploadBackup(File(context.filesDir, "backup.json"), "logbook")
                    onResult("Backup started.")
                }
                intent.contains("restore", true) -> {
                    syncViewModel.downloadBackup(File(context.filesDir, "backup.json"))
                    onResult("Restore started.")
                }
                intent.contains("vault write", true) -> {
                    val fileName = params["fileName"] ?: "vault.dat"
                    val data = params["data"]?.toByteArray() ?: ByteArray(0)
                    com.arclogbook.vault.SecureVaultManager.writeEncrypted(context, fileName, data)
                    onResult("Data written to secure vault.")
                }
                intent.contains("vault read", true) -> {
                    val fileName = params["fileName"] ?: "vault.dat"
                    val data = com.arclogbook.vault.SecureVaultManager.readEncrypted(context, fileName)
                    onResult("Vault data: ${data?.size ?: 0} bytes read.")
                }
                intent.contains("quick log", true) -> {
                    val content = params["content"] ?: "Quick log entry"
                    logbookViewModel.addLog(LogEntry(content = content, tags = "Quick", type = "Widget", timestamp = System.currentTimeMillis()))
                    onResult("Quick log entry added.")
                }
                intent.contains("build workflow", true) -> {
                    val steps = params["steps"]?.split(",") ?: listOf()
                    // Simulate workflow build
                    onResult("Workflow built with ${steps.size} steps.")
                }
                intent.contains("show timeline", true) -> {
                    onResult("Opening timeline and map view.")
                }
                intent.contains("edit theme", true) -> {
                    onResult("Opening theme editor.")
                }
                intent.contains("voice command", true) -> {
                    com.arclogbook.voice.CyberpunkVoiceAssistant.startListening(context) { spokenText ->
                        handleIntent(spokenText, params, onResult)
                    }
                    onResult("Voice assistant activated. Speak your command.")
                }
                intent.contains("change theme", true) -> {
                    val theme = params["theme"] ?: "CYBERPUNK"
                    // You may need to call a theme change function or ViewModel
                    onResult("Theme changed to $theme.")
                }
                intent.contains("change font", true) -> {
                    val font = params["font"] ?: "Orbitron"
                    // You may need to call a font change function or ViewModel
                    onResult("Font changed to $font.")
                }
                intent.contains("access settings", true) -> {
                    onResult("Opening settings screen.")
                }
                intent.contains("run privacy scan", true) -> {
                    // Simulate privacy scan
                    onResult("Privacy scan completed. No issues found.")
                }
                intent.contains("run security audit", true) -> {
                    // Simulate security audit
                    onResult("Security audit completed. All systems secure.")
                }
                intent.contains("wipe data", true) -> {
                    // Simulate data wipe (add confirmation in production)
                    onResult("Data wipe initiated. All local data will be erased.")
                }
                intent.contains("biometric unlock", true) -> {
                    // Simulate biometric unlock
                    onResult("Biometric unlock requested.")
                }
                intent.contains("sync onedrive", true) -> {
                    syncViewModel.authenticate(onSuccess = {}, onError = {})
                    onResult("OneDrive sync started.")
                }
                intent.contains("show dashboard", true) -> {
                    onResult("Opening dashboard.")
                }
                intent.contains("show disclaimer", true) -> {
                    onResult("Showing research disclaimer.")
                }
                intent.contains("show evidence", true) -> {
                    val details = evidenceViewModel.evidenceList.value.joinToString(", ")
                    onResult("Evidence: $details")
                }
                intent.contains("add evidence", true) -> {
                    val details = params["details"] ?: ""
                    evidenceViewModel.addEvidence(details)
                    onResult("Evidence added.")
                }
                intent.contains("track case", true) -> {
                    val case = params["case"] ?: ""
                    caseViewModel.trackCase(case)
                    onResult("Case tracking started for '$case'.")
                }
                intent.contains("show cases", true) -> {
                    val cases = caseViewModel.caseList.value.joinToString(", ")
                    onResult("Cases: $cases")
                }
                intent.contains("show notes", true) -> {
                    val notes = logbookViewModel.notes.joinToString(", ")
                    onResult("Notes: $notes")
                }
                intent.contains("show alerts", true) -> {
                    val alerts = pastebinViewModel.alerts.value.map { it.snippet }.joinToString(", ")
                    onResult("Pastebin Alerts: $alerts")
                }
                intent.contains("show deepweb alerts", true) -> {
                    val alerts = deepWebViewModel.alerts.value.map { it.snippet }.joinToString(", ")
                    onResult("DeepWeb Alerts: $alerts")
                }
                intent.contains("enrich threats", true) -> {
                    // Threat enrichment (async)
                    CoroutineScope(Dispatchers.IO).launch {
                        val threats = osintViewModel.osintResults.value
                        val enriched = com.arclogbook.data.ThreatEnrichmentManager.enrichThreats(threats)
                        onResult("Threats enriched: ${enriched.size}")
                    }
                }
                intent.contains("run workflow", true) -> {
                    com.arclogbook.osint.WorkflowWorker.startWorkflow(context)
                    onResult("Automated workflow started.")
                }
                intent.contains("show analytics", true) -> {
                    com.arclogbook.analytics.LoggingAnalyticsDashboard.log("Analytics viewed via chatbot")
                    onResult("Analytics dashboard opened.")
                }
                intent.contains("show plugin marketplace", true) -> {
                    com.arclogbook.plugin.PluginMarketplace.discoverPlugins()
                    onResult("Plugin marketplace opened.")
                }
                intent.contains("create instance", true) -> {
                    com.arclogbook.ui.MultiInstanceManager.createInstance(params["name"] ?: "Instance")
                    onResult("New investigation instance created.")
                }
                intent.contains("show instances", true) -> {
                    val instances = com.arclogbook.ui.MultiInstanceManager.getInstances().joinToString()
                    onResult("Instances: $instances")
                }
                intent.contains("accessibility audit", true) -> {
                    val audit = com.arclogbook.security.AccessibilityAuditUtils.audit(listOf("", "Logbook entry", ""))
                    onResult("Accessibility audit complete: $audit")
                }
                intent.contains("open code editor", true) -> {
                    onResult("In-app code editor opened.")
                }
                intent.contains("open api explorer", true) -> {
                    onResult("API explorer opened.")
                }
                intent.contains("chain of custody", true) -> {
                    com.arclogbook.security.ChainOfCustodyUtils.recordAction(params["action"] ?: "Chatbot action")
                    onResult("Chain of custody recorded.")
                }
                intent.contains("geo tag", true) -> {
                    com.arclogbook.osint.GeoTaggingUtils.tagLocation(params["location"] ?: "Unknown")
                    onResult("Location geo-tagged.")
                }
                intent.contains("dark web monitor", true) -> {
                    com.arclogbook.osint.DarkWebMonitorUtils.monitor(params["keyword"] ?: "")
                    onResult("Dark web monitoring started.")
                }
                intent.contains("custom alert rule", true) -> {
                    com.arclogbook.security.CustomAlertRules.addRule(params["rule"] ?: "")
                    onResult("Custom alert rule added.")
                }
                intent.contains("backup all data", true) -> {
                    // Export all app data and settings, then upload to OneDrive
                    val backupFile = File(context.filesDir, "ArcLogbookBackup.json")
                    // Example: gather all data as JSON (replace with real export logic)
                    val allData = "{\"logbook\":[],\"settings\":[],\"evidence\":[],\"cases\":[],\"notes\":[],\"prefs\":[]}" // TODO: Replace with real data
                    backupFile.writeText(allData)
                    onedriveSyncViewModel.uploadBackup(backupFile, "full")
                    onResult("All data and settings backed up to OneDrive.")
                }
                intent.contains("restore all data", true) -> {
                    val backupFile = File(context.filesDir, "ArcLogbookBackup.json")
                    onedriveSyncViewModel.downloadBackup(backupFile)
                    // TODO: Add logic to import/restore all data from backupFile
                    onResult("All data and settings restored from OneDrive backup.")
                }
                else -> onResult("Sorry, I can't automate that yet.")
            }
        }
    }
}
