package com.example.jetpackcompose.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity

/**
 * PopupServiceManager is responsible for managing the lifecycle of the PopupService.
 * It handles permission requests for posting notifications and starting the service.
 * @property context The context in which the service manager operates, typically an Activity context.
 */
class PopupServiceManager(private val context: Context) {

    /**
     * Requests permission to post notifications. If granted, it starts the PopupService.
     * If denied, it shows a toast message to inform the user.
     */
    fun requestPermission() {
        val requestPermissionLauncher =
            (context as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) startPopupService()
                else Toast.makeText(context, "Permission denied, notifications won't work", Toast.LENGTH_LONG).show()
            }

        // Request permission for posting notifications on Android 13 (API level 33) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Starts the PopupService as a foreground service.
     */
    fun startPopupService() {
        val serviceIntent = Intent(context, PopupService::class.java)
        context.startForegroundService(serviceIntent)
    }
}