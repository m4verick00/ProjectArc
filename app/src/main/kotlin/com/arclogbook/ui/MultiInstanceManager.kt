package com.arclogbook.ui

object MultiInstanceManager {
    private val instances = mutableListOf<String>()

    fun createInstance(name: String) {
        instances.add(name)
    }

    fun getInstances(): List<String> = instances.toList()
    fun closeInstance(name: String) {
        instances.remove(name)
    }
}
