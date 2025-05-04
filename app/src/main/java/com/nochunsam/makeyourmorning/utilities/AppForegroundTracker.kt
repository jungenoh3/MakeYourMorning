package com.nochunsam.makeyourmorning.utilities

import android.app.Activity
import android.app.Application
import android.os.Bundle

// https://velog.io/@jmseb3/Android-ForegroundBackground-%EA%B5%AC%EB%B6%84%ED%95%98%EA%B8%B0
class AppForegroundTracker: Application.ActivityLifecycleCallbacks {
    companion object {
        private var instance: AppForegroundTracker? = null

        fun getInstance(): AppForegroundTracker {
            if (instance == null){
                instance = AppForegroundTracker()
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