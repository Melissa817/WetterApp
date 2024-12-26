package com.example.jetpackcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.viewmodel.WeatherViewModel
import com.example.jetpackcompose.ui.WeatherApp
import com.example.jetpackcompose.service.PopupServiceManager

/**
 * MainActivity is the main entry point of the Jetpack Compose weather application.
 * It sets up the user interface and manages the lifecycle of the PopupService.
 */
class MainActivity : ComponentActivity() {

    private val popupServiceManager = PopupServiceManager(this)

    /**
     * Called when the activity is created. Initializes the activity, handles the popup service,
     * and sets the content view to the WeatherApp composable.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the popup service based on the Android version
        handlePopupService()

        // Set the content view to the WeatherApp composable
        setContent {
            val viewModel: WeatherViewModel = viewModel()
            WeatherApp(viewModel)
        }
    }

    /**
     * Handles the initialization of the PopupService. Requests notification permission
     * for Android 13 (API level 33) and above, or starts the service directly for lower versions.
     */
    private fun handlePopupService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            popupServiceManager.requestPermission()
        } else {
            popupServiceManager.startPopupService()
        }
    }
}