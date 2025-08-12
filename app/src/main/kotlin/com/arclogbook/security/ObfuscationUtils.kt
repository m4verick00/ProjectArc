package com.arclogbook.security

object ObfuscationUtils {
    fun obfuscateString(input: String): String {
        // Simple XOR obfuscation for demo
        val key = 0x5A
        return input.map { (it.code xor key).toChar() }.joinToString("")
    }
    fun deobfuscateString(input: String): String {
        val key = 0x5A
        return input.map { (it.code xor key).toChar() }.joinToString("")
    }
}
