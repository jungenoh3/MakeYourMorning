package com.yesnoheun3.makeyourmorning.pages.wakeup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.pages.wakeup.screen.WakeUpManagerScreen

class WakeUpManagerActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WakeUpManagerScreen()
        }
    }
}