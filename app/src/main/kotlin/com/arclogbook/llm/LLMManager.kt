package com.arclogbook.llm

import android.content.Context
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.LinkedHashMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LLMManager(private val context: Context) {
    private val modelFileName = "phi-2.Q4_K_M.gguf"
    private var isLoaded = false
    private var useGpuDelegate: Boolean = false

    // Simple LRU cache for recent prompts -> responses (avoids recomputation for identical prompts)
    private val cache = object : LinkedHashMap<String, String>(32, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean = size > 50
    }

    fun loadModel(gpu: Boolean = false): Boolean {
        val assetManager = context.assets
        val modelFile = File(context.filesDir, modelFileName)
        if (!modelFile.exists()) {
            assetManager.open(modelFileName).use { input ->
                modelFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        // TODO: Initialize GGML/LLM JNI or ONNX runtime here
        useGpuDelegate = gpu
        isLoaded = true // Set to true if model loads successfully
        return isLoaded
    }

    fun runInference(prompt: String): String {
        if (!isLoaded) loadModel()
        synchronized(cache) { cache[prompt]?.let { return it } }
        // TODO: Call GGML/LLM JNI/ONNX runtime with prompt and return output (respect useGpuDelegate)
        val result = "[LLM] $prompt"
        synchronized(cache) { cache[prompt] = result }
        return result
    }

    /** Stream inference tokens (simulated) returning a cold Flow of incremental text. */
    fun streamInference(prompt: String, delayPerTokenMs: Long = 40L): Flow<String> = callbackFlow {
        if (!isLoaded) loadModel()
        val cached = synchronized(cache) { cache[prompt] }
        if (cached != null) {
            trySend(cached)
            close(); return@callbackFlow
        }
        // Simulate tokenization by splitting on whitespace
        val tokens = ("[LLM] " + prompt).split(" ")
        val builder = StringBuilder()
        for (t in tokens) {
            builder.append(t).append(' ')
            trySend(builder.toString())
            delay(delayPerTokenMs)
        }
        val final = builder.toString().trimEnd()
        synchronized(cache) { cache[prompt] = final }
        close()
    }
}
