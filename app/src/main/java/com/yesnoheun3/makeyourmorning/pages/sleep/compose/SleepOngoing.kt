package com.yesnoheun3.makeyourmorning.pages.sleep.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.compose.CustomColumn
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun SleepOngoing(){
    val blockingEndTime = FocusBlockingManager.blockingEndTime
    val blockingEndDateTime = remember(blockingEndTime) {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(blockingEndTime),
            ZoneId.systemDefault()
        )
    }

    val endTime = remember { blockingEndDateTime }
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            if (currentTime.isAfter(endTime) || currentTime.isEqual(endTime)){
                break
            }
            currentTime = LocalDateTime.now()
            FocusBlockingManager.checkBlocking()
            delay(1000L)
        }
    }

    val remainingDuration = Duration.between(currentTime, endTime)
    val minutesLeft = remainingDuration.toMinutes()
    val secondsLeft = remainingDuration.seconds % 60

    CustomColumn (color = PurpleGrey80) {
        Text(text = "주무세요!", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text("남은 시간: ${minutesLeft}: ${secondsLeft}", fontSize = 20.sp)
        Button(onClick = {
            FocusBlockingManager.stopBlocking()
        }) {
            Text(text = "취소")
        }
    }
}