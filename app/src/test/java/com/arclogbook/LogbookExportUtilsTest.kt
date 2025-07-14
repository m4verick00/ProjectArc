package com.arclogbook

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogbookExportUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class LogbookExportUtilsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testExportToJsonCreatesFile() {
        val entries = listOf(LogEntry(content = "Test", type = "OSINT", timestamp = 1, tags = "test"))
        val file = LogbookExportUtils.exportToJson(context, entries)
        assertTrue(file.exists() && file.readText().contains("Test"))
    }
}
