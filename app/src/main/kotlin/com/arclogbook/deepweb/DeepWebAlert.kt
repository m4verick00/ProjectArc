package com.arclogbook.deepweb

import kotlinx.serialization.Serializable

@Serializable
data class DeepWebAlert(
    val id: Int = 0,
    val keyword: String,
    val source: String, // e.g., Pastebin, Dread, Ahmia, etc.
    val snippet: String,
    val timestamp: Long,
    val tags: String = "DeepWeb"
)
