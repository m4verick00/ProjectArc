package com.arclogbook.plugin

interface Plugin {
    val name: String
    fun execute(): Any
}

object PluginManager {
    private val plugins = mutableListOf<Plugin>()
    fun register(plugin: Plugin) {
        plugins.add(plugin)
    }
    fun getPlugins(): List<Plugin> = plugins
}
