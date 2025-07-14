package com.arclogbook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResearchDisclaimerScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Research Mode Disclaimer", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text("This app is intended for ethical research and educational purposes only.\n\n" +
                "- Do not use for illegal activity.\n" +
                "- Do not buy, sell, or distribute illegal data.\n" +
                "- Always respect privacy and local laws.\n" +
                "- Document your sources and methods.\n\n" +
                "By using this app, you agree to use it responsibly and ethically.")
    }
}
