package com.arclogbook.threatintel

object ThreatIntelFeeds {
    fun fetchMISPFeed(): List<String> = listOf("MISP Alert 1", "MISP Alert 2")
    fun fetchOpenCTIFeed(): List<String> = listOf("OpenCTI Alert 1", "OpenCTI Alert 2")
}
