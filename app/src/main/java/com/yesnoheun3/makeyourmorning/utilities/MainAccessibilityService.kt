package com.yesnoheun3.makeyourmorning.utilities

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent

// TODO 권한이 필요하다!!!!
class MainAccessibilityService: AccessibilityService() {

    private val allowedApps = setOf(
        "com.yesnoheun3.makeyourmorning", // Your app
        "com.google.android.apps.nexuslauncher", // Launcher
        "com.google.android.googlequicksearchbox",
        "com.android.systemui",
    )

    override fun onServiceConnected() {
        super.onServiceConnected()

        // TODO 추후에 package는 별도로 추가

        println("Accessibility Service conntected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()

            if (packageName != null && packageName !in allowedApps) {
                Log.d("AppMonitorService", "Detected unauthorized app: $packageName")

                if (!OverlayService.isRunning) {
                    Log.d("AppMonitorService", "Starting OverlayService")
                    val intent = Intent(this, OverlayService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    } else {
                        startService(intent)
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }


}