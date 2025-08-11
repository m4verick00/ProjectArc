package com.arclogbook.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

/** Simple window info for adaptive layouts (dual-pane on large width). */
@Composable
fun rememberIsWideScreen(wideDp: Int = 840): Boolean {
    val config = LocalConfiguration.current
    return remember(config.screenWidthDp) { config.screenWidthDp >= wideDp }
}
