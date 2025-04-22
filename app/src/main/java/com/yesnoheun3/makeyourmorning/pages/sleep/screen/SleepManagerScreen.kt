package com.yesnoheun3.makeyourmorning.pages.sleep.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.yesnoheun3.makeyourmorning.pages.sleep.compose.SleepOngoing
import com.yesnoheun3.makeyourmorning.pages.sleep.compose.SleepPreparation
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager

@Composable
fun SleepManagerScreen() {
    val isSleeping by FocusBlockingManager.isBlocking.collectAsState()

    if (isSleeping){
        SleepOngoing()
    } else {
        SleepPreparation()
    }
}