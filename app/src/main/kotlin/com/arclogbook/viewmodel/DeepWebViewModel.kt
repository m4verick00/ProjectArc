package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.deepweb.DeepWebAlert
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
class DeepWebViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<DeepWebAlert>>(emptyList())
    val alerts: StateFlow<List<DeepWebAlert>> = _alerts

    val pagedAlerts: Flow<PagingData<DeepWebAlert>> = Pager(
        PagingConfig(pageSize = 25, enablePlaceholders = false, initialLoadSize = 50)
    ) { InMemoryDeepWebPagingSource { _alerts.value } }.flow.cachedIn(viewModelScope)

    val sources = listOf(
        "Pastebin", "Ghostbin", "Dread (onion)", "Ahmia (onion)", "Telegram", "BreachForums", "IntelX", "OnionPaste"
    )

    fun searchDeepWeb(keyword: String, source: String) {
        try {
            // Simulate a deep web alert
            val alert = DeepWebAlert(
                keyword = keyword,
                source = source,
                snippet = "Simulated hit for '$keyword' on $source.",
                timestamp = System.currentTimeMillis()
            )
            _alerts.update { it + alert }
        } catch (e: Exception) {
            GlobalErrorLogger.logError(
                error = e,
                context = "searchDeepWeb",
                userAction = "Keyword: $keyword, Source: $source",
            )
        }
    }

    fun saveAlertToLogbook(alert: DeepWebAlert) = viewModelScope.launch {
        val logEntry = LogEntry(
            content = "[${alert.source}] ${alert.snippet}",
            type = "Dark Web",
            timestamp = alert.timestamp,
            tags = alert.tags
        )
        logEntryDao.insert(logEntry)
    }
}
