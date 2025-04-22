package com.yesnoheun3.makeyourmorning.pages.sleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.pages.sleep.screen.SleepOverlayScreen

class SleepOverlayActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SleepOverlayScreen(onDismiss = { finish() })
        }
    }
}