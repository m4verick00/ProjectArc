package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import com.arclogbook.security.GlobalErrorLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Shodan-style device / port discovery for user-owned IP addresses only.
 * Ethical safeguards: consent, confirmation, logging, rate limiting (basic), biometric gating via UI.
 */
@HiltViewModel
class DeviceScanViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao,
    private val workManager: WorkManager
) : ViewModel() {

    data class DevicePort(val port: Int, val service: String)
    data class DeviceFinding(
        val ip: String,
        val org: String?,
        val isp: String?,
        val country: String?,
        val openPorts: List<DevicePort>,
        val vulns: List<String>,
        val raw: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _findings = MutableStateFlow<List<DeviceFinding>>(emptyList())
    val findings: StateFlow<List<DeviceFinding>> = _findings

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _consent = MutableStateFlow(false)
    val consent: StateFlow<Boolean> = _consent

    private val _biometricUnlocked = MutableStateFlow(false)
    val biometricUnlocked: StateFlow<Boolean> = _biometricUnlocked

    // Basic request throttle
    private var lastRequestTs = 0L
    private val minIntervalMs = 5_000L

    fun acceptConsent() { _consent.value = true; audit("CONSENT_ACCEPTED", "Device scan consent accepted") }

    fun setBiometricUnlocked(unlocked: Boolean) { _biometricUnlocked.value = unlocked }

    fun scheduleBackgroundScan(ip: String, apiKey: String) {
        if (!validateInput(ip, apiKey)) return
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(false)
            .build()
        val work = OneTimeWorkRequestBuilder<DeviceScanWorker>()
            .setInputData(workDataOf("ip" to ip, "apiKey" to apiKey))
            .addTag("device_scan_$ip")
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork("device_scan_$ip", ExistingWorkPolicy.REPLACE, work)
        audit("SCAN_SCHEDULED", ip)
    }

    fun runImmediateScan(ip: String, apiKey: String) {
        if (!validateInput(ip, apiKey)) return
        if (System.currentTimeMillis() - lastRequestTs < minIntervalMs) {
            _error.value = "Please wait before scanning again"
            return
        }
        lastRequestTs = System.currentTimeMillis()
        _isScanning.value = true
        viewModelScope.launch {
            try {
                val finding = queryShodan(ip, apiKey)
                _findings.value = _findings.value + finding
                persistFinding(finding)
                audit("SCAN_COMPLETE", ip)
            } catch (e: Exception) {
                _error.value = e.message
                GlobalErrorLogger.logError(e, context = "runImmediateScan", userAction = "IP=$ip")
                audit("SCAN_ERROR", e.message ?: "Unknown")
            } finally {
                _isScanning.value = false
            }
        }
    }

    private fun validateInput(ip: String, apiKey: String): Boolean {
        if (!_consent.value) { _error.value = "Consent required"; return false }
        if (!_biometricUnlocked.value) { _error.value = "Unlock required"; return false }
        if (ip.isBlank() || !ip.matches(Regex("^(?:\\d{1,3}\\.){3}\\d{1,3}$"))) { _error.value = "Invalid IPv4"; return false }
        if (apiKey.isBlank()) { _error.value = "API key required"; return false }
        return true
    }

    private fun queryShodan(ip: String, apiKey: String): DeviceFinding {
        // Direct HTTPS call to Shodan host endpoint
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        val url = "https://api.shodan.io/shodan/host/$ip?key=$apiKey"
        val req = Request.Builder().url(url).get().build()
        client.newCall(req).execute().use { resp ->
            val bodyStr = resp.body?.string() ?: "{}"
            if (!resp.isSuccessful) throw IllegalStateException("Shodan error ${resp.code}: $bodyStr")
            val json = JSONObject(bodyStr)
            val portsJson = json.optJSONArray("ports")
            val ports = mutableListOf<DevicePort>()
            if (portsJson != null) {
                for (i in 0 until portsJson.length()) ports += DevicePort(portsJson.getInt(i), "")
            }
            val vulnsJson = json.optJSONObject("vulns")
            val vulns = mutableListOf<String>()
            if (vulnsJson != null) vulns += vulnsJson.keys().asSequence().toList()
            return DeviceFinding(
                ip = ip,
                org = json.optString("org"),
                isp = json.optString("isp"),
                country = json.optString("country_name"),
                openPorts = ports,
                vulns = vulns,
                raw = bodyStr
            )
        }
    }

    private fun persistFinding(f: DeviceFinding) = viewModelScope.launch {
        val summary = buildString {
            append("IP ${f.ip} Ports:${f.openPorts.joinToString { it.port.toString() }} Vulns:${f.vulns.size}")
        }
        logEntryDao.insert(
            LogEntry(
                content = summary.take(900),
                type = "DEVICE_SCAN",
                timestamp = f.timestamp,
                tags = "scan,device,${f.ip}"
            )
        )
    }

    private fun audit(event: String, detail: String) = viewModelScope.launch {
        logEntryDao.insert(
            LogEntry(
                content = "$event:$detail".take(900),
                type = "SCAN_AUDIT",
                timestamp = System.currentTimeMillis(),
                tags = "audit,device_scan"
            )
        )
    }
}

class DeviceScanWorker(appContext: android.content.Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // Minimal placeholder; actual logic can reuse repository abstraction.
        val ip = inputData.getString("ip") ?: return Result.failure()
        // Real scanning moved to ViewModel triggered by UI; background scanning could be implemented here.
        return Result.success(workDataOf("ip" to ip))
    }
}
