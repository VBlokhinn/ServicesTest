package ru.sumin.servicestest

import android.app.IntentService
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

class MyIntentService2 : IntentService(NAME) {


    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        val page = p0?.getIntExtra(EXTRA_PAGE, 0) ?: 0
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer $i page: $page")
        }

    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }


    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService: $message")
    }


    companion object {

        private const val NAME = "my_intent_service"
        private const val EXTRA_PAGE = "EXTRA_PAGE"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(EXTRA_PAGE, page)
            }
        }
    }
}