package com.arclogbook.threat

import com.arclogbook.data.LogEntry

object ThreatEnrichmentManager {
    fun enrich(entry: LogEntry): LogEntry {
        // Example: Auto-tagging and risk scoring
        val risk = when {
            entry.content.contains("leak", true) -> "High"
            entry.content.contains("password", true) -> "Critical"
            entry.content.contains("public", true) -> "Medium"
            else -> "Low"
        }
        val newTags = entry.tags + if (!entry.tags.contains(risk)) ", $risk" else ""
        return entry.copy(tags = newTags.trim(','))
    }
}
