package com.yesnoheun3.makeyourmorning.utilities.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.pages.day.NightOverlayActivity
import com.yesnoheun3.makeyourmorning.pages.day.MorningOverlayActivity
import com.yesnoheun3.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO 권한이 필요하다!!!!
class AppBlockAccessibilityService : AccessibilityService() {
    private lateinit var repository: AppRepository
    private val defaultAllowedApps  = mutableSetOf(
        "com.yesnoheun3.makeyourmorning", // Your app
        "com.google.android.apps.nexuslauncher", // Launcher
        "com.google.android.googlequicksearchbox",
        "com.android.systemui",
    )
    private val allowedApps = mutableSetOf<String>()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override fun onServiceConnected() {
        super.onServiceConnected()
        repository = AppRepository(application)

        // Collect installed apps and add to allowedApps
        serviceScope.launch {
            repository.getAllInstalledApp()
                .collectLatest { dbApps ->
                    synchronized(allowedApps) {
                        allowedApps.clear()
                        allowedApps.addAll(defaultAllowedApps)
                        allowedApps.addAll(dbApps)
                    }
                }
        }
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
        synchronized(allowedApps) {
            if (packageName !in allowedApps) {
                Log.d("BlockService", "Blocking $packageName")
                returnToHomeScreen()
                showBlockingScreen()
            }
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
        val blockIntent = if (FocusBlockingManager.getBlockType == BlockType.NIGHT) {
            Intent(this, NightOverlayActivity::class.java)
        } else {
            Intent(this, MorningOverlayActivity::class.java)
        }
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        blockIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(blockIntent)
    }
}