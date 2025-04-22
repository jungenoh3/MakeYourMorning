package com.yesnoheun3.makeyourmorning.pages.sleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.pages.sleep.screen.SleepManagerScreen

class SleepManagerActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SleepManagerScreen()
        }
    }
}