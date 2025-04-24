package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.data.BlockTime
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AppBlockAccessibilityService
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import java.time.LocalDateTime

@Composable
fun CustomPreparation(
    blockTimeId: MutableState<LocalDateTime>,
    scheduler: AlarmScheduler,
    contentText: String,
    buttonText: String,
    backgroundColor: Color,
    buttonColor: Color,
    blockType: BlockType
) {
    val context = LocalContext.current
    var isAccessibilityEnabled by remember {
        mutableStateOf(
            AccessibilityServiceChecker.isAccessibilityServiceEnabled(
                context,
                AppBlockAccessibilityService::class.java
            )
        )
    }
    var selectedMinute by remember { mutableStateOf(30) }

    CustomColumn(color = backgroundColor) {
        Switch(
            checked = isAccessibilityEnabled,
            onCheckedChange = {
                AccessibilityServiceChecker.requestAccessibilityPermissionIfNeeded(
                    context,
                    AppBlockAccessibilityService::class.java
                )
                isAccessibilityEnabled = AccessibilityServiceChecker.isAccessibilityServiceEnabled(
                    context,
                    AppBlockAccessibilityService::class.java
                )
            }
        )
        Text(text = contentText, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))

        MinutePicker(
            selectedMinute = selectedMinute,
            onMinuteChanged = { selectedMinute = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = {
                blockTimeId.value = LocalDateTime.now()
                val blockTime = BlockTime(
                    id = blockTimeId.value.toString(),
                    minute = selectedMinute,
                    type = blockType
                )
                scheduler.scheduleBlock(blockTime)
                // FocusBlockingManager.startBlockingFor(1 * 10 * 1000L) // 5초 (분 * 초 * 1000L)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.Black
            ),
        ) {
            Text(
                text = buttonText,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}