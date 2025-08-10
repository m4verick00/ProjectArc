// Privacy: This module does not collect, transmit, or use device identifiers (IMEI, MAC, Android ID, etc). All data is stored securely and privately. See ArcLogbook privacy policy for details.

package com.arclogbook.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "deep_web_alerts",
    indices = [
        Index(value = ["timestamp"], name = "idx_deep_web_alerts_timestamp"),
        Index(value = ["severity"], name = "idx_deep_web_alerts_severity")
    ]
)
data class DeepWebAlert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Date,
    val source: String,
    val severity: Int
)