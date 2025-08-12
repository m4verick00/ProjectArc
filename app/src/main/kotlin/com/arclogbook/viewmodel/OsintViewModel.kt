package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import com.arclogbook.osint.OsintResult
import com.arclogbook.osint.OsintSources
import com.arclogbook.network.RetrofitInstance
import com.arclogbook.security.GlobalErrorLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OsintViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao
) : ViewModel() {
    private val _osintResults = MutableStateFlow<List<OsintResult>>(emptyList())
    val osintResults: StateFlow<List<OsintResult>> = _osintResults

    // In-memory paging for now (simulate until persisted). If persistence added, swap to PagingSource DAO.
    val pagedOsintResults: Flow<PagingData<OsintResult>> = Pager(
        PagingConfig(pageSize = 30, enablePlaceholders = false, initialLoadSize = 60)
    ) { InMemoryOsintPagingSource { _osintResults.value } }.flow.cachedIn(viewModelScope)

    val availableSources = OsintSources.sources

    var errorMessage: String = ""
    fun runOsintQuery(target: String, queryType: String, source: String) {
        viewModelScope.launch {
            try {
                // Input validation
                if (target.isBlank() || target.length > 256) {
                    errorMessage = "Target must be non-empty and < 256 chars."
                    return@launch
                }
                when (source) {
                    "HaveIBeenPwned (breach check)" -> {
                        // Example: Real API call (requires API key)
                        val response = RetrofitInstance.api.checkEmailBreach(target)
                        val resultText = if (response.isSuccessful) {
                            "Breaches found: ${response.body()?.size ?: 0}"
                        } else {
                            "No breach data or error."
                        }
                        val result = OsintResult(
                            target = target,
                            queryType = queryType,
                            source = source,
                            result = resultText,
                            timestamp = System.currentTimeMillis()
                        )
                        _osintResults.update { it + result }
                    }
                    "Shodan (India)" -> {
                        // Example: Real API call (requires API key)
                        val apiKey = "YOUR_SHODAN_API_KEY"
                        val response = RetrofitInstance.api.shodanHostInfo(target, apiKey)
                        val resultText = if (response.isSuccessful) {
                            response.body().toString()
                        } else {
                            "No Shodan data or error."
                        }
                        val result = OsintResult(
                            target = target,
                            queryType = queryType,
                            source = source,
                            result = resultText,
                            timestamp = System.currentTimeMillis()
                        )
                        _osintResults.update { it + result }
                    }
                    "CERT-In (Indian Cyber Alerts)" -> {
                        val response = RetrofitInstance.api.getCertInAlerts()
                        val resultText = if (response.isSuccessful) {
                            response.body()?.take(500) ?: "No alerts."
                        } else {
                            "No CERT-In data or error."
                        }
                        val result = OsintResult(
                            target = target,
                            queryType = queryType,
                            source = source,
                            result = resultText,
                            timestamp = System.currentTimeMillis()
                        )
                        _osintResults.update { it + result }
                    }
                    else -> {
                        // Simulate for other sources
                        val result = OsintResult(
                            target = target,
                            queryType = queryType,
                            source = source,
                            result = "Simulated result for $target ($queryType) from $source",
                            timestamp = System.currentTimeMillis()
                        )
                        _osintResults.update { it + result }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "OSINT query failed: ${e.message}".take(128)
                GlobalErrorLogger.logError(
                    error = e,
                    context = "runOsintQuery",
                    userAction = "Query: $target, Type: $queryType, Source: $source",
                )
            }
        }
    }

    fun saveResultToLogbook(osintResult: OsintResult) = viewModelScope.launch {
        val logEntry = LogEntry(
            content = "[${osintResult.source}] ${osintResult.result}",
            type = "OSINT",
            timestamp = osintResult.timestamp,
            tags = osintResult.tags
        )
        logEntryDao.insert(logEntry)
    }
}
