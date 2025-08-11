package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.pastebin.PastebinAlert
import com.arclogbook.data.LogEntryDao
import com.arclogbook.data.LogEntry
import com.arclogbook.security.GlobalErrorLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PastebinViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<PastebinAlert>>(emptyList())
    val alerts: StateFlow<List<PastebinAlert>> = _alerts

    val pagedAlerts: Flow<PagingData<PastebinAlert>> = Pager(
        PagingConfig(pageSize = 25, enablePlaceholders = false, initialLoadSize = 50)
    ) { InMemoryPastebinPagingSource { _alerts.value } }.flow.cachedIn(viewModelScope)

    val sources = listOf("Pastebin", "Ghostbin", "Telegram")

    var errorMessage: String = ""
    fun monitorKeyword(keyword: String, source: String) {
        try {
            if (keyword.isBlank() || keyword.length > 128) {
                errorMessage = "Keyword must be non-empty and < 128 chars."
                return
            }
            // Simulate a pastebin alert
            val alert = PastebinAlert(
                keyword = keyword,
                source = source,
                snippet = "Simulated paste for '$keyword' on $source.",
                timestamp = System.currentTimeMillis()
            )
            _alerts.update { it + alert }
        } catch (e: Exception) {
            errorMessage = "Pastebin monitor failed: ${e.message}".take(128)
            GlobalErrorLogger.logError(
                error = e,
                context = "monitorKeyword",
                userAction = "Keyword: $keyword, Source: $source",
            )
        }
    }
    fun saveAlertToLogbook(alert: PastebinAlert) = viewModelScope.launch {
        try {
            val logEntry = LogEntry(
                content = "[${alert.source}] ${alert.snippet}",
                type = "Pastebin",
                timestamp = alert.timestamp,
                tags = alert.tags
            )
            logEntryDao.insert(logEntry)
            errorMessage = ""
        } catch (e: Exception) {
            errorMessage = "Failed to save alert: ${e.message}".take(128)
        }
    }
}
