package com.arclogbook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LogEntryDao {
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC")
    fun getAll(): Flow<List<LogEntry>>

    @Query("SELECT * FROM log_entries WHERE tags LIKE '%' || :tag || '%' OR content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun search(tag: String, keyword: String): Flow<List<LogEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LogEntry)

    @Update
    suspend fun update(entry: LogEntry)

    @Delete
    suspend fun delete(entry: LogEntry)

    @Query("DELETE FROM log_entries")
    suspend fun clearAll()
}
