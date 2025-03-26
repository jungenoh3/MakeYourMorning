package com.yesnoheun3.makeyourmorning

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MainApp: Application() {

    private lateinit var foregroundDetector: MainLifeCycleCallback

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

        foregroundDetector = MainLifeCycleCallback.getInstance()
        registerActivityLifecycleCallbacks(foregroundDetector)
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(foregroundDetector)
        super.onTerminate()
    }
}

// https://velog.io/@jmseb3/Android-ForegroundBackground-%EA%B5%AC%EB%B6%84%ED%95%98%EA%B8%B0
class MainLifeCycleCallback: Application.ActivityLifecycleCallbacks {
    companion object {
        private var instance: MainLifeCycleCallback? = null

        fun getInstance(): MainLifeCycleCallback {
            if (instance == null){
                instance = MainLifeCycleCallback()
            }
            return instance!!
        }
    }

    private var numStarted = 0
    private var isAppRunning = false

    val isForeground: Boolean get() = isAppRunning

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0){
            isAppRunning = true
        }
        numStarted += 1
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        numStarted -= 1
        if (numStarted == 0){
            isAppRunning = false
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}