package com.arclogbook.security

import java.util.concurrent.ConcurrentLinkedQueue

object SecurityAuditDashboard {
    private val events = ConcurrentLinkedQueue<String>()

    fun logEvent(event: String) {
        events.add("[SECURITY EVENT] $event")
    }

    fun getEvents(): List<String> = events.toList()
    fun clearEvents() = events.clear()
}
