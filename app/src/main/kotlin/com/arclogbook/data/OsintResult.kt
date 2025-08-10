// Privacy: This module does not collect, transmit, or use device identifiers (IMEI, MAC, Android ID, etc). All data is stored securely and privately. See ArcLogbook privacy policy for details.

package com.arclogbook.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "osint_results",
    indices = [
        Index(value = ["timestamp"], name = "idx_osint_results_timestamp"),
        Index(value = ["severity"], name = "idx_osint_results_severity")
    ]
)
data class OsintResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Date,
    val source: String,
    val severity: Int
)