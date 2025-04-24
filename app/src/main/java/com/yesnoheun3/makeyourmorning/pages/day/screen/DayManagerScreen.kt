package com.yesnoheun3.makeyourmorning.pages.day.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.yesnoheun3.makeyourmorning.common.compose.CustomOngoing
import com.yesnoheun3.makeyourmorning.common.compose.CustomPreparation
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun DayManagerScreen(){
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)
    val blockTimeId = remember { mutableStateOf(LocalDateTime.now()) }
    val isBlocking by FocusBlockingManager.isBlocking.collectAsState()
    val blockType by FocusBlockingManager.blockType.collectAsState()

    if (isBlocking){
        if (blockType == BlockType.NIGHT){
            CustomOngoing(
                blockTimeId = blockTimeId.value,
                scheduler = scheduler,
                backgroundColor = PurpleGrey80,
                buttonColor = Purple40,
                contentText = "폰을 끄고 주무세요!"
            )
        } else if (blockType == BlockType.MORNING) {
            CustomOngoing(
                blockTimeId = blockTimeId.value,
                scheduler = scheduler,
                backgroundColor = Yellow80,
                buttonColor = Yellow60,
                contentText = "눈 앞의 일에 집중하세요!"
            )
        }
    } else {
        CustomPreparation(
            blockTimeId = blockTimeId,
            scheduler = scheduler,
            blockType = blockType
        )
    }

}