package com.arclogbook.llm

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class LLMManagerStreamTest {
    @Test
    fun streamEmitsProgressively() = runTest {
        val ctx = Mockito.mock(Context::class.java)
        val assets = Mockito.mock(android.content.res.AssetManager::class.java)
        Mockito.`when`(ctx.assets).thenReturn(assets)
        Mockito.`when`(ctx.filesDir).thenReturn(java.io.File("build/tmp/llmTest2").apply { mkdirs() })
        Mockito.`when`(assets.open("phi-2.Q4_K_M.gguf")).thenReturn(ByteArrayInputStream(ByteArray(0)))
        val mgr = LLMManager(ctx)
        val final = mgr.streamInference("hello world test").last()
        assertTrue(final.contains("hello"))
        assertTrue(final.contains("world"))
    }
}
