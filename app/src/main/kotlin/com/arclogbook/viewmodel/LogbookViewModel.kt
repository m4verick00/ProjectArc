package com.arclogbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import com.arclogbook.security.GlobalErrorLogger
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

    var errorMessage: String = ""
    fun addLog(entry: LogEntry) = viewModelScope.launch {
        try {
            // Input validation
            if (entry.content.isBlank() || entry.content.length > 1000) {
                errorMessage = "Log content must be non-empty and < 1000 chars."
                return@launch
            }
            logEntryDao.insert(entry)
            errorMessage = ""
        } catch (e: Exception) {
            errorMessage = "Failed to add log: ${e.message}".take(128)
            GlobalErrorLogger.logError(
                error = e,
                context = "addLog",
                userAction = "Adding log entry: ${entry.content}",
            )
        }
    }

    fun deleteLog(entry: LogEntry) = viewModelScope.launch {
        try {
            logEntryDao.delete(entry)
            errorMessage = ""
        } catch (e: Exception) {
            errorMessage = "Failed to delete log: ${e.message}".take(128)
            GlobalErrorLogger.logError(
                error = e,
                context = "deleteLog",
                userAction = "Deleting log entry: ${entry.content}",
            )
        }
    }

    fun searchLogs(tag: String, keyword: String): StateFlow<List<LogEntry>> =
        logEntryDao.search(tag, keyword)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _notes = mutableListOf<String>()
    private val _evidence = mutableListOf<String>()
    private val _cases = mutableListOf<String>()
    private val _undoStack = mutableListOf<LogEntry>()
    private val _redoStack = mutableListOf<LogEntry>()

    fun addNote(note: String) {
        if (note.isNotBlank()) _notes.add(note)
    }
    fun getNotes(): List<String> = _notes

    fun addEvidence(evidence: String) {
        if (evidence.isNotBlank()) _evidence.add(evidence)
    }
    fun getEvidence(): List<String> = _evidence

    fun addCase(case: String) {
        if (case.isNotBlank()) _cases.add(case)
    }
    fun getCases(): List<String> = _cases

    fun undoLastLog() {
        if (logEntries.value.isNotEmpty()) {
            val last = logEntries.value.last()
            _undoStack.add(last)
            deleteLog(last)
        }
    }
    fun redoLastLog() {
        if (_undoStack.isNotEmpty()) {
            val last = _undoStack.removeAt(_undoStack.size - 1)
            addLog(last)
            _redoStack.add(last)
        }
    }
}
