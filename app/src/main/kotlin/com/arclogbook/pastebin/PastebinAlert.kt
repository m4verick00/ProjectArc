package com.arclogbook.pastebin

import kotlinx.serialization.Serializable

@Serializable
data class PastebinAlert(
    val id: Int = 0,
    val keyword: String,
    val source: String, // e.g., Pastebin, Ghostbin, Telegram
    val snippet: String,
    val timestamp: Long,
    val tags: String = "Pastebin"
)
