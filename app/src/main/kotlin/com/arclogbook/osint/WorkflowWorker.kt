package com.arclogbook.osint

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.Constraints
import com.arclogbook.osint.WorkflowNotificationUtils
import kotlinx.coroutines.runBlocking

class WorkflowWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        // Battery-efficient: batch network calls, minimize wakeups
        // Only run when device is charging and on unmetered network
        val steps = listOf<suspend () -> String>(
            { "Step 1: HIBP" },
            { "Step 2: Shodan" },
            { "Step 3: Censys" }
        )
        val results = runBlocking { AutomatedWorkflowUtils.runWorkflow(steps) }
        // Advanced chaining: run additional step if all succeed
        if (results.all { it.isNotBlank() }) {
            val extraStep = runBlocking { "Step 4: Extra OSINT" }
            // Save extraStep result
        }
        // Save results to file/db/log
        WorkflowNotificationUtils.showNotification(applicationContext, "Workflow Complete", "Results: ${results.joinToString()}" )
        return Result.success()
    }

    override fun getForegroundInfo(): ForegroundInfo {
        // Optional: show notification if running long
        return ForegroundInfo(
            System.currentTimeMillis().toInt(),
            WorkflowNotificationUtils.createRunningNotification(applicationContext)
        )
    }

    companion object {
        fun startWorkflow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build()
            val request = OneTimeWorkRequestBuilder<WorkflowWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
