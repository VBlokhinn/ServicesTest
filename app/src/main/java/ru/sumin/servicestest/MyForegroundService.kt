package ru.sumin.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : android.app.Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        } else {
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, createNotification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log("Timer $i")
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        log("onDestroy")
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService: $message")
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification() : Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "ForegroundChannel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        private const val EXTRA_START = "EXTRA_START"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}