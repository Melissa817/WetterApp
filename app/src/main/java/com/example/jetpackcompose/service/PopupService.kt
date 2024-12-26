package com.example.jetpackcompose.service

import android.app.*
import android.content.*
import android.os.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import androidx.core.content.ContextCompat
import com.example.jetpackcompose.MainActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.jetpackcompose.ui.views.dataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * PopupService is a foreground service that sends notifications at specified intervals.
 * It retrieves the timer option from a data store and updates the notification frequency
 * based on user preferences.
 */
class PopupService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var delayMillis: Long = -1L
    private var i = 0
    private val dataStore by lazy { applicationContext.dataStore }
    private var isNotificationEnabled: Boolean = false

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val newTimerOption = intent?.getStringExtra("timer_option") ?: "Deactivated"
            updateTimerOption(newTimerOption)
        }
    }

    /**
     * Called when the service is created. Initializes the notification channel,
     * starts the foreground service, registers the update receiver, and initializes
     * the timer from settings.
     */
    override fun onCreate() {
        super.onCreate()
        val notification = getNotification("Service is running")
        createNotificationChannel()
        startForeground(1, notification)
        registerUpdateReceiver()
        initializeTimerFromSettings()
    }

    /**
     * Called when the service is destroyed. Cleans up resources by removing callbacks
     * and unregistering the update receiver.
     */
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showNotificationRunnable)
        unregisterReceiver(updateReceiver)
    }

    /**
     * Called when the service is started. Posts the notification runnable if the delay
     * is set.
     *
     * @param intent The intent that started the service.
     * @param flags Additional flags about how the service should be started.
     * @param startId A unique integer representing the start request.
     * @return An integer indicating how to continue the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (delayMillis != -1L) {
            handler.removeCallbacks(showNotificationRunnable)
            handler.post(showNotificationRunnable)
        }
        return START_STICKY
    }

    /**
     * Binds the service to an activity. This implementation returns null as the service
     * is not designed to be bound.
     *
     * @param intent The intent used to bind to this service.
     * @return An IBinder for the service.
     */
    override fun onBind(intent: Intent?): IBinder? = null

    private val showNotificationRunnable = object : Runnable {
        override fun run() {
            if (isNotificationEnabled) {
                sendNotification("Hello World $i")
                i++
            }
            handler.postDelayed(this, delayMillis)
        }
    }

    /**
     * Updates the timer option based on user preferences.
     *
     * @param option The new timer option as a string.
     */
    private fun updateTimerOption(option: String) {
        delayMillis = timerOptionToMillis(option)
        isNotificationEnabled = delayMillis != -1L
        handler.removeCallbacks(showNotificationRunnable)

        if (delayMillis == -1L) {
            stopSelf()
        } else {
            handler.postDelayed(showNotificationRunnable, delayMillis)
        }
    }

    /**
     * Fetches the timer option from the data store asynchronously.
     *
     * @return The timer option as a string.
     */
    private suspend fun fetchTimerOptionFromSettings(): String {
        val key = stringPreferencesKey("timer_option_key")
        val timerOption = dataStore.data.map { preferences ->
            preferences[key] ?: "Deactivated"
        }.first()

        return timerOption
    }

    /**
     * Registers a broadcast receiver to listen for timer updates.
     */
    private fun registerUpdateReceiver() {
        ContextCompat.registerReceiver(
            this,
            updateReceiver,
            IntentFilter("com.example.jetpackcompose.UPDATE_TIMER"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    /**
     * Converts a timer option string to milliseconds.
     *
     * @param option The timer option as a string.
     * @return The corresponding delay in milliseconds.
     */
    private fun timerOptionToMillis(option: String): Long {
        return when (option) {
            "10s" -> 10_000L
            "30s" -> 30_000L
            "60s" -> 60_000L
            "30 min" -> 30 * 60 * 1000L
            "60 min" -> 60 * 60 * 1000L
            else -> -1L
        }
    }

/**
 * Initializes the timer from settings by fetching the timer option
 * and starting the notification runnable if the option is valid.
 */
private fun initializeTimerFromSettings() {
    CoroutineScope(Dispatchers.IO).launch {
        val timerOption = fetchTimerOptionFromSettings()
        delayMillis = timerOptionToMillis(timerOption)

        if (delayMillis != -1L) {
            isNotificationEnabled = true
            handler.post(showNotificationRunnable)
        }
    }
}

/**
 * Sends a notification with the specified message.
 *
 * @param message The message to be displayed in the notification.
 */
private fun sendNotification(message: String) {
    if (ActivityCompat.checkSelfPermission(
            this@PopupService,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val notificationManager = NotificationManagerCompat.from(this)
    val notification = getNotification(message)
    notificationManager.notify(1, notification)
}

/**
 * Builds and returns a notification with the specified content text.
 *
 * @param contentText The text to be displayed in the notification.
 * @return A Notification object.
 */
private fun getNotification(contentText: String): Notification {
    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    return NotificationCompat.Builder(this, "popup_service_channel")
        .setContentTitle("Popup Service")
        .setContentText(contentText)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
}

/**
 * Creates a notification channel for Android O and above.
 */
private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "popup_service_channel",
            "Popup Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications from Popup Service"
            enableLights(true)
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
}