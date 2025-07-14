package com.arclogbook.alerts

data class AlertRule(
    val keyword: String = "",
    val tag: String = "",
    val source: String = "",
    val enabled: Boolean = true
)
