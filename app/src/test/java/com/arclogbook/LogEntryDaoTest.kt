package com.arclogbook

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.arclogbook.data.ArcLogbookDatabase
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LogEntryDaoTest {
    private lateinit var db: ArcLogbookDatabase
    private lateinit var dao: LogEntryDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ArcLogbookDatabase::class.java).build()
        dao = db.logEntryDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetLogEntry() = runBlocking {
        val entry = LogEntry(content = "Test", type = "OSINT", timestamp = 1, tags = "test")
        dao.insert(entry)
        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals("Test", all[0].content)
    }

    @Test
    fun deleteLogEntry() = runBlocking {
        val entry = LogEntry(content = "Test", type = "OSINT", timestamp = 1, tags = "test")
        dao.insert(entry)
        dao.delete(entry)
        val all = dao.getAll().first()
        assertEquals(0, all.size)
    }
}
