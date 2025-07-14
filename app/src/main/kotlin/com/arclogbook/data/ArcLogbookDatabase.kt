package com.arclogbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1, exportSchema = false)
abstract class ArcLogbookDatabase : RoomDatabase() {
    abstract fun logEntryDao(): LogEntryDao

    companion object {
        @Volatile
        private var INSTANCE: ArcLogbookDatabase? = null

        fun getDatabase(context: Context): ArcLogbookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArcLogbookDatabase::class.java,
                    "arclogbook_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
