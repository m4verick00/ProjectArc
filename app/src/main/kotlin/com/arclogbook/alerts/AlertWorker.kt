package com.arclogbook.alerts

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arclogbook.viewmodel.LogbookViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val logbookViewModel: LogbookViewModel
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // TODO: Load alert rules and check for new log entries matching rules
        // If match, call AlertManager.showAlert(...)
        return Result.success()
    }
}
