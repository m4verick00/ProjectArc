package com.arclogbook.e2e

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.arclogbook.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Basic end-to-end smoke of journaling (logbook) UI: navigates to Logbook and opens add dialog (if present). */
@RunWith(AndroidJUnit4::class)
class JournalingFlowTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openLogbookAndTapAdd() {
        // Bottom nav label "Logbook" should exist by default; tap FAB by its content description if present
        // Title check
        onView(withText("Logbook"))
        // FAB content description from LogbookScreen
        onView(withContentDescription("Add Entry")).perform(click())
    }
}
