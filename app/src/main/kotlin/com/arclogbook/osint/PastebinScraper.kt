package com.arclogbook.osint

import java.net.URL

object PastebinScraper {
    fun fetchLatestPastes(): List<String> {
        // Example: fetches latest public pastes (for demo purposes)
        val url = "https://scrape.pastebin.com/api_scraping.php?limit=10"
        return URL(url).readText().lines()
    }
}
