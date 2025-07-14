package com.arclogbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.onedrive.OneDriveSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OneDriveSyncViewModel @Inject constructor(
    app: Application
) : AndroidViewModel(app) {
    private val syncManager = OneDriveSyncManager(app)
    private val _syncStatus = MutableStateFlow("")
    val syncStatus: StateFlow<String> = _syncStatus

    fun authenticate(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        syncManager.authenticate(
            onSuccess = { _syncStatus.value = "Authenticated"; onSuccess() },
            onError = { e -> _syncStatus.value = "Auth failed: ${e.message}"; onError(e) }
        )
    }

    fun uploadBackup(file: File, infoType: String) {
        _syncStatus.value = "Uploading..."
        syncManager.uploadBackup(file, infoType) { success ->
            _syncStatus.value = if (success) "Backup uploaded" else "Upload failed"
        }
    }

    fun downloadBackup(destFile: File) {
        _syncStatus.value = "Downloading..."
        syncManager.downloadBackup(destFile) { success ->
            _syncStatus.value = if (success) "Backup restored" else "Download failed"
        }
    }
}
