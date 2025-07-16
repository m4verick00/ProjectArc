package com.arclogbook.data

// Privacy: No device identifiers (IMEI, MAC, Android ID) are collected or transmitted in this module.
// Only log entry data is stored and managed securely.

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "log_entries")
@Serializable
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val type: String, // OSINT / Dark Web
    val timestamp: Long,
    val tags: String // Comma-separated tags
)
