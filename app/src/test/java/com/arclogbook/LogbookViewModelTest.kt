package com.arclogbook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import com.arclogbook.viewmodel.LogbookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class LogbookViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var dao: LogEntryDao
    private lateinit var viewModel: LogbookViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dao = mock(LogEntryDao::class.java)
        `when`(dao.getAll()).thenReturn(flowOf(listOf()))
        viewModel = LogbookViewModel(dao)
    }

    @Test
    fun testAddLog() = testScope.runTest {
        val entry = LogEntry(content = "Test", type = "OSINT", timestamp = 0, tags = "test")
        viewModel.addLog(entry)
        verify(dao).insert(entry)
    }

    @Test
    fun testDeleteLog() = testScope.runTest {
        val entry = LogEntry(content = "Test", type = "OSINT", timestamp = 0, tags = "test")
        viewModel.deleteLog(entry)
        verify(dao).delete(entry)
    }

    @Test
    fun testSearchLogs() = testScope.runTest {
        `when`(dao.search("tag", "keyword")).thenReturn(flowOf(listOf()))
        val result = viewModel.searchLogs("tag", "keyword")
        assertNotNull(result)
    }
}
