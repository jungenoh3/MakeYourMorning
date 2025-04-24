package com.yesnoheun3.makeyourmorning.pages.wakeup.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.yesnoheun3.makeyourmorning.common.compose.CustomOngoing
import com.yesnoheun3.makeyourmorning.common.compose.CustomPreparation
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import java.time.LocalDateTime

@Composable
fun WakeUpManagerScreen(){
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)
    val blockTimeId = remember { mutableStateOf(LocalDateTime.now()) }
    val isSleeping by FocusBlockingManager.isBlocking.collectAsState()

    if (isSleeping){
        CustomOngoing(
            blockTimeId = blockTimeId.value,
            scheduler = scheduler,
            backgroundColor = Yellow80,
            buttonColor = Yellow60,
            contentText = "눈 앞의 일에 집중하세요!"
        )
    } else {
        CustomPreparation(
            blockTimeId = blockTimeId,
            scheduler = scheduler,
            backgroundColor = Yellow80,
            buttonColor = Yellow60,
            contentText = "좋은 아침이에요!",
            buttonText = "일어나기",
            blockType = BlockType.MORNING
        )
    }
}