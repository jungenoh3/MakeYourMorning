package com.yesnoheun3.makeyourmorning.utilities

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.yesnoheun3.makeyourmorning.pages.sleep.SleepActivity

// TODO 권한이 필요하다!!!!
class AppBlockAccessibilityService: AccessibilityService() {

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
        if (!FocusBlockingManager.isBlockingActive()) {
            println("Service is not on")
            return
        }

        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString() ?: return
        if (packageName !in allowedApps){
            Log.d("BlockService", "Blocking ${packageName}")
            showBlockingScreen()
        }
    }


    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private fun showBlockingScreen(){
        val blockIntent = Intent(this, SleepActivity::class.java)
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(blockIntent)
    }

}