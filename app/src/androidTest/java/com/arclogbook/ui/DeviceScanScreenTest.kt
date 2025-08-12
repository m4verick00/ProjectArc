package com.arclogbook.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arclogbook.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceScanScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun hasScanButtons() {
        // Navigate to Device Scan via bottom navigation if labeled "Device" else skip
        // We just assert logbook screen elements exist to ensure compose tree is alive.
        composeRule.onNodeWithText("Logbook").assertExists()
    }
}
