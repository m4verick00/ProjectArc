package com.arclogbook.llm

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import java.io.ByteArrayInputStream

class LLMManagerTest {
    @Test
    fun cacheAndLoadModel() {
        val ctx = Mockito.mock(Context::class.java)
        val assets = Mockito.mock(android.content.res.AssetManager::class.java)
        Mockito.`when`(ctx.assets).thenReturn(assets)
        Mockito.`when`(ctx.filesDir).thenReturn(java.io.File("build/tmp/llmTest").apply { mkdirs() })
        Mockito.`when`(assets.open("phi-2.Q4_K_M.gguf")).thenReturn(ByteArrayInputStream(ByteArray(0)))

        val mgr = LLMManager(ctx)
        val first = mgr.runInference("hello")
        val second = mgr.runInference("hello")
        assertEquals(first, second) // cached
        assertTrue(first.contains("hello"))
    }
}
