package com.arclogbook.osint

import kotlinx.serialization.Serializable

@Serializable
data class OsintResult(
    val id: Int = 0,
    val target: String,
    val queryType: String, // e.g., username, email, domain
    val source: String,    // OSINT source (e.g., MCA, CERT-In, etc.)
    val result: String,    // JSON or formatted string
    val timestamp: Long,
    val tags: String = "OSINT"
)
