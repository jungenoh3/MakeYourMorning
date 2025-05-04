package com.yesnoheun3.makeyourmorning.pages.day.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.yesnoheun3.makeyourmorning.common.compose.CustomOngoing
import com.yesnoheun3.makeyourmorning.common.compose.CustomPreparation
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import java.time.LocalDateTime

@Composable
fun DayManagerScreen(){
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)
    val blockTimeId = remember { mutableStateOf(LocalDateTime.now()) }
    val isBlocking by FocusBlockingManager.isBlocking.collectAsState()
    val blockType by FocusBlockingManager.blockType.collectAsState()

    if (isBlocking){
        CustomOngoing (
            type = blockType,
            buttonText = "취소",
            isOverlay = false,
            onDismiss = {
                scheduler.cancel(blockTimeId.value.toString())
                FocusBlockingManager.stopBlocking()
            }
        )
    } else {
        CustomPreparation(
            blockTimeId = blockTimeId,
            scheduler = scheduler,
            blockType = blockType
        )
    }

}