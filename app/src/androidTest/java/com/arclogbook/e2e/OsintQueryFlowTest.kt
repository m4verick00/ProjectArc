package com.arclogbook.e2e

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.arclogbook.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Simulated OSINT query flow smoke: navigates to OSINT via bottom nav (label OSINT Toolkit) and performs a search input. */
@RunWith(AndroidJUnit4::class)
class OsintQueryFlowTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun runOsintSearch() {
        // Navigate if needed - relies on bottom nav label
        onView(withText("OSINT")).perform(click())
        // Search field placeholder text "Search logs" reused; attempt input
        onView(withText("Search logs")).perform(replaceText("email@example.com"))
    }
}
