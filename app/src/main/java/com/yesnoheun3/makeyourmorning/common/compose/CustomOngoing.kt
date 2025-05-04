package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.ui.theme.secondaryContainerLight
import com.yesnoheun3.makeyourmorning.ui.theme.tertiaryDarkHighContrast
import com.yesnoheun3.makeyourmorning.ui.theme.tertiaryDeepDarkHighContrast
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CustomOngoing(
    type: BlockType = BlockType.NIGHT,
    buttonText: String,
    isOverlay: Boolean,
    onDismiss: () -> Unit,
) {
    val blockingEndTime = FocusBlockingManager.blockingEndTime
    val blockingEndDateTime = remember(blockingEndTime) {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(blockingEndTime),
            ZoneId.systemDefault()
        )
    }
    val backgroundColor = if (type == BlockType.NIGHT) secondaryContainerLight else tertiaryDarkHighContrast
    val buttonColor = if (type == BlockType.NIGHT) MaterialTheme.colorScheme.primaryContainer else tertiaryDeepDarkHighContrast
    val buttonTextColor = if (type == BlockType.NIGHT) Color.White else Color.Black

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
        if (isOverlay) onDismiss()
    }

    val remainingDuration = Duration.between(currentTime, endTime)
    val minutesLeft = remainingDuration.toMinutes()
    val secondsLeft = remainingDuration.seconds % 60

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(30.dp),
    ) {
        Text("남은 시간: ${minutesLeft}: ${secondsLeft}", fontSize = 15.sp, modifier = Modifier.align(Alignment.Center).offset(y = (-40).dp))
        Text("휴대폰을 끄세요!", fontSize = 40.sp, modifier = Modifier.align(Alignment.Center))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
            )
        ) {
            Text(
                text = buttonText,
                fontSize = 20.sp,
            )
        }
    }
}