package com.arclogbook.ui.components

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/** Global one-way event bus to surface transient messages as Snackbars. */
data class SnackbarMessage(val text: String, val kind: Kind = Kind.Info, val actionLabel: String? = null)
enum class Kind { Info, Success, Warning, Error }

object SnackbarEvents {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _messages = MutableSharedFlow<SnackbarMessage>(extraBufferCapacity = 16)
    val messages: SharedFlow<SnackbarMessage> = _messages

    fun dispatch(message: String, kind: Kind = Kind.Info, actionLabel: String? = null) {
        if (message.isBlank()) return
        val sm = SnackbarMessage(message, kind, actionLabel)
        if (!_messages.tryEmit(sm)) scope.launch { _messages.emit(sm) }
    }
}
