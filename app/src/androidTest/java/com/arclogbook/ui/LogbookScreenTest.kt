package com.arclogbook.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arclogbook.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogbookScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchFiltersList() {
        // Precondition: some seeded data would appear; for now ensure search field exists and accepts input.
        composeRule.onNodeWithText("Search logs").performTextInput("test")
        // Without real seed we just validate input didn't crash.
    }
}
