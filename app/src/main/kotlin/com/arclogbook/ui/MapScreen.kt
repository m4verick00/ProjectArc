package com.arclogbook.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(factory = { context ->
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
            MapView(context).apply {
                setMultiTouchControls(true)
            }
        }, modifier = Modifier.fillMaxSize())
    }
}
