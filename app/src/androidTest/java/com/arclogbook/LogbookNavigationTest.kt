package com.arclogbook

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arclogbook.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogbookNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigateToSettings() {
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.onNodeWithText("Theme").assertIsDisplayed()
    }

    @Test
    fun testNavigateToLogbook() {
        composeTestRule.onNodeWithText("Logbook").performClick()
        composeTestRule.onNodeWithText("Add New Entry").assertIsDisplayed()
    }
}
