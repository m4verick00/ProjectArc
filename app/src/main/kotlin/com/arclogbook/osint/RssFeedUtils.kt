package com.arclogbook.osint

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

object RssFeedUtils {
    fun fetchRssFeed(url: String): List<String> {
        // Simple RSS fetcher, returns list of titles
        val result = mutableListOf<String>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(URL(url).openStream(), null)
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "title") {
                result.add(parser.nextText())
            }
            eventType = parser.next()
        }
        return result
    }
}
