package com.nochunsam.makeyourmorning.utilities

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class AppInitializer: Application() {

    private lateinit var foregroundDetector: AppForegroundTracker

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =  NotificationChannel(
                "make_your_morning5134",
                "sleep time alarm",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        foregroundDetector = AppForegroundTracker.getInstance()
        registerActivityLifecycleCallbacks(foregroundDetector)
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(foregroundDetector)
        super.onTerminate()
    }
}