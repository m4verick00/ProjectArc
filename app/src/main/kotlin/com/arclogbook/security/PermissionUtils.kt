package com.arclogbook.security

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

object PermissionUtils {
    fun hasStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
    fun requestStoragePermission(launcher: ActivityResultLauncher<String>) {
        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
