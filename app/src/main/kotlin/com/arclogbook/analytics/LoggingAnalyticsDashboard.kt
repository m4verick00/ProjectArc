package com.arclogbook.analytics

object LoggingAnalyticsDashboard {
    private val logs = mutableListOf<String>()
    fun log(event: String) { logs.add(event) }
    fun getLogs(): List<String> = logs.toList()
    fun clearLogs() = logs.clear()
}
