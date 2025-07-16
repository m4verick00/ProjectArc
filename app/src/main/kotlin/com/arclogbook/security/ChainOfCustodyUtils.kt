package com.arclogbook.security

import java.util.UUID

object ChainOfCustodyUtils {
    data class ChainEvent(val id: String, val action: String, val timestamp: Long)
    private val chainEvents = mutableListOf<ChainEvent>()

    fun logEvent(action: String) {
        chainEvents.add(ChainEvent(UUID.randomUUID().toString(), action, System.currentTimeMillis()))
    }

    fun getChain(): List<ChainEvent> = chainEvents.toList()
    fun clearChain() = chainEvents.clear()
}
