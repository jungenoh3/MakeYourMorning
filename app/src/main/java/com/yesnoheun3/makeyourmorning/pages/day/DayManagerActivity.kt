package com.yesnoheun3.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.pages.day.screen.DayManagerScreen
import com.yesnoheun3.makeyourmorning.ui.theme.MakeYourMorningTheme

class DayManagerActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                DayManagerScreen()
            }
        }
    }
}