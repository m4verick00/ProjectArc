package com.arclogbook.security

object CustomAlertRules {
    private val rules = mutableListOf<(String) -> Boolean>()
    private val triggeredAlerts = mutableListOf<String>()

    fun addRule(rule: (String) -> Boolean) {
        rules.add(rule)
    }

    fun checkAlerts(data: String) {
        rules.forEach { rule ->
            if (rule(data)) triggeredAlerts.add(data)
        }
    }

    fun getTriggeredAlerts(): List<String> = triggeredAlerts.toList()
    fun clearAlerts() = triggeredAlerts.clear()
}
