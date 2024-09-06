package ru.sumin.servicestest

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : android.app.Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        log("onStartCommand")
        coroutineScope.launch {
            for (i in start until 100) {
                delay(1000)
                log("Timer $i")
            }
        }

        return START_REDELIVER_INTENT
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
        Log.d("SERVICE_TAG", "MyService: $message")
    }

    companion object {

        private const val EXTRA_START = "EXTRA_START"

        fun newIntent(context: Context, num: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, num)
            }
        }
    }
}