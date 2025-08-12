package com.arclogbook.osint

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkflowScheduler {
    fun schedulePeriodicWorkflow(context: Context, intervalMinutes: Long = 60) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()
        val request = PeriodicWorkRequestBuilder<WorkflowWorker>(intervalMinutes, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    fun scheduleAdaptiveWorkflow(context: Context, lastRunMillis: Long) {
        val now = System.currentTimeMillis()
        val intervalMinutes = when {
            now - lastRunMillis < 30 * 60 * 1000 -> 120L // If run recently, wait longer
            now - lastRunMillis < 2 * 60 * 60 * 1000 -> 60L
            else -> 30L // If not run for a while, run sooner
        }
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()
        val request = PeriodicWorkRequestBuilder<WorkflowWorker>(intervalMinutes, java.util.concurrent.TimeUnit.MINUTES, 15, java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
