package com.arclogbook.security

object AccessibilityAuditUtils {
    fun audit(contentDescriptions: List<String>): List<String> {
        return contentDescriptions.filter { it.isBlank() }
    }
    fun suggestImprovements(contentDescriptions: List<String>): List<String> {
        return contentDescriptions.mapNotNull {
            if (it.isBlank()) "Missing contentDescription" else null
        }
    }
}
