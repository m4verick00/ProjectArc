package com.arclogbook.osint

/**
 * List of OSINT sources and resources, with a focus on Indian utilization.
 * Used to populate query options and document available sources in the app.
 */
object OsintSources {
    val sources = listOf(
        // Social Media
        "Facebook (public)",
        "Twitter/X (public)",
        "Instagram (public)",
        "LinkedIn (public)",
        "Telegram (public channels)",
        "Reddit (r/india, r/IndianCyberSec)",
        // Government & Legal
        "MCA (Ministry of Corporate Affairs)",
        "CERSAI (Asset Registry)",
        "SEBI (Market Alerts)",
        "ECI (Election Commission of India)",
        "Indian Court Records (eCourts)",
        "RTI Online (Public Info)",
        // Cybersecurity
        "CERT-In (Indian Cyber Alerts)",
        "National Cyber Crime Reporting Portal",
        "Pastebin/Ghostbin (leaked Indian data)",
        // Domain & IP
        "Whois Lookup",
        "Registry.in (IN domains)",
        "Shodan (India)",
        "Censys (India)",
        // News & Breach Data
        "Indian News Portals",
        "HaveIBeenPwned (breach check)",
        "Data Breach Forums (public)",
        // Phone & Email
        "Truecaller (public info)",
        "Email Verification Tools",
        // Public Data
        "Google Dorks (.in)",
        "GitHub/GitLab (Indian orgs)",
        "Wayback Machine",
        // Financial & Regulatory
        "RBI (Bank lists, alerts)",
        "SEBI Investor Complaints",
        // Miscellaneous
        "Indian Railways PNR Status",
        "VAHAN (Vehicle Registration)",
        "PAN/Aadhaar Verification (public info)"
    )
}
