package com.arclogbook.viewmodel

import app.cash.turbine.test
import com.arclogbook.data.LogEntry
import com.arclogbook.data.LogEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LogbookViewModelTest {
    private val dao = Mockito.mock(LogEntryDao::class.java)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addLog_rejectsInvalid() = runTest(dispatcher) {
        whenever(dao.getAll()).thenReturn(flowOf(emptyList()))
        val vm = LogbookViewModel(dao)
        vm.addLog(LogEntry(content = "", type = "OSINT", timestamp = 1L, tags = "a"))
        assertEquals("Log content must be non-empty and < 1000 chars.", vm.errorMessage)
    }

    @Test
    fun addLog_acceptsValid() = runTest(dispatcher) {
        whenever(dao.getAll()).thenReturn(flowOf(emptyList()))
        val vm = LogbookViewModel(dao)
        vm.addLog(LogEntry(content = "ok", type = "OSINT", timestamp = 1L, tags = "t"))
        assertEquals("", vm.errorMessage)
    }
}
