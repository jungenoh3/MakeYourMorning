package com.yesnoheun3.makeyourmorning.utilities

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.yesnoheun3.makeyourmorning.R
import com.yesnoheun3.makeyourmorning.pages.sleep.OverlayCompose

class OverlayService: LifecycleService(), SavedStateRegistryOwner {
    companion object {
        var isRunning = false
    }

    private lateinit var windowManager: WindowManager
    private lateinit var composeView: ComposeView
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()

        isRunning = true

        System.out.println("Overlay Service Started")

        val notification = NotificationCompat.Builder(applicationContext, "make_your_morning5134")
            .setContentText("bbbbbbbbbbb")
            .setContentTitle("For overlay Service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(2, notification)

        savedStateRegistryController.performAttach()
        savedStateRegistryController.performRestore(null)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        composeView = ComposeView(this).apply {
            setViewTreeSavedStateRegistryOwner(this@OverlayService)
            setViewTreeLifecycleOwner(this@OverlayService)
            setContent {
                OverlayCompose {
                    stopSelf()
                }
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        if (composeView?.parent == null) {
            windowManager.addView(composeView, params)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        isRunning = false
        windowManager.removeView(composeView)
        super.onDestroy()
    }
}