package com.arclogbook.data

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
