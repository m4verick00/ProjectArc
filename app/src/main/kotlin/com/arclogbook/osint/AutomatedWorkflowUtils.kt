package com.arclogbook.osint

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AutomatedWorkflowUtils {
    suspend fun runWorkflow(steps: List<suspend () -> String>): List<String> {
        val results = mutableListOf<String>()
        for (step in steps) {
            val result = withContext(Dispatchers.IO) { step() }
            results.add(result)
        }
        return results
    }
}
