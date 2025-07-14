package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.pastebin.PastebinAlert
import com.arclogbook.data.LogEntryDao
import com.arclogbook.data.LogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PastebinViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<PastebinAlert>>(emptyList())
    val alerts: StateFlow<List<PastebinAlert>> = _alerts

    val sources = listOf("Pastebin", "Ghostbin", "Telegram")

    fun monitorKeyword(keyword: String, source: String) {
        // Simulate a pastebin alert
        val alert = PastebinAlert(
            keyword = keyword,
            source = source,
            snippet = "Simulated paste for '$keyword' on $source.",
            timestamp = System.currentTimeMillis()
        )
        _alerts.value = _alerts.value + alert
    }

    fun saveAlertToLogbook(alert: PastebinAlert) = viewModelScope.launch {
        val logEntry = LogEntry(
            content = "[${alert.source}] ${alert.snippet}",
            type = "Pastebin",
            timestamp = alert.timestamp,
            tags = alert.tags
        )
        logEntryDao.insert(logEntry)
    }
}
