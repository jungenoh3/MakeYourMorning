package com.nochunsam.makeyourmorning.utilities.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.nochunsam.makeyourmorning.pages.day.OverlayActivity
import com.nochunsam.makeyourmorning.utilities.database.AppRepository

// TODO 권한이 필요하다!!!!
class AppBlockAccessibilityService : AccessibilityService() {
    private lateinit var repository: AppRepository

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository = AppRepository(application)

        println("Accessibility Service conntected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (FocusBlockingManager.isBlocking.value.not()) {
            println("Service is not on")
            return
        }

        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString() ?: return
        if (packageName !in FocusBlockingManager.allowedAppList) {
            Log.d("BlockService", "Blocking $packageName")
            returnToHomeScreen()
            showBlockingScreen()
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private fun returnToHomeScreen() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
    }

    private fun showBlockingScreen() {
        val blockIntent = Intent(this, OverlayActivity::class.java)
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(blockIntent)
    }
}