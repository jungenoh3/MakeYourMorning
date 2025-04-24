package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CustomOngoing(
    blockTimeId: LocalDateTime,
    scheduler: AlarmScheduler,
    backgroundColor: Color,
    buttonColor: Color,
    contentText: String
) {
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
            if (currentTime.isAfter(endTime) || currentTime.isEqual(endTime)) {
                break
            }
            currentTime = LocalDateTime.now()
            delay(1000L)
        }
        FocusBlockingManager.checkBlocking()
    }

    val remainingDuration = Duration.between(currentTime, endTime)
    val minutesLeft = remainingDuration.toMinutes()
    val secondsLeft = remainingDuration.seconds % 60

    CustomColumn(color = backgroundColor) {
        Text(text = contentText, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text("남은 시간: ${minutesLeft}: ${secondsLeft}", fontSize = 20.sp)
        Button(
            onClick = {
                scheduler.cancel(blockTimeId.toString())
                FocusBlockingManager.stopBlocking()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.Black
            )
        ) {
            Text(text = "취소")
        }
    }
}