package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val logEntryDao: LogEntryDao
) : ViewModel() {
    val logEntries: StateFlow<List<LogEntry>> = logEntryDao.getAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addLog(entry: LogEntry) = viewModelScope.launch {
        logEntryDao.insert(entry)
    }

    fun deleteLog(entry: LogEntry) = viewModelScope.launch {
        logEntryDao.delete(entry)
    }

    fun searchLogs(tag: String, keyword: String): StateFlow<List<LogEntry>> =
        logEntryDao.search(tag, keyword)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
