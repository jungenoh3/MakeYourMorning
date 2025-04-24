package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CustomOverlayScreen(onDismiss: () -> Unit, backgroundColor: Color, buttonColor: Color){
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
            delay(1000L)
        }
        FocusBlockingManager.checkBlocking()
        onDismiss()
    }

    val remainingDuration = Duration.between(currentTime, endTime)
    val minutesLeft = remainingDuration.toMinutes()
    val secondsLeft = remainingDuration.seconds % 60

    CustomColumn (color = backgroundColor) {
        Text("휴대폰을 끄세요!")
        Text("남은 시간: ${minutesLeft}: ${secondsLeft}", fontSize = 20.sp)
        Button(onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.Black)
            ) {
            Text(text = "화면 닫기")
        }
    }
}