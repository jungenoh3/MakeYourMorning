package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yesnoheun3.makeyourmorning.common.data.BlockTime
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AppBlockAccessibilityService
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun CustomPreparation(
    blockTimeId: MutableState<LocalDateTime>,
    scheduler: AlarmScheduler,
    blockType: BlockType
) {
    val context = LocalContext.current
    val isAccessibilityEnabledFlow = remember { MutableStateFlow(false) }
    val isAccessibilityEnabled by isAccessibilityEnabledFlow.collectAsState()
    var selectedMinute by remember { mutableStateOf(30) }

    // TODO 공부
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(isAccessibilityEnabled) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAccessibilityEnabledFlow.value =
                    AccessibilityServiceChecker.isAccessibilityServiceEnabled(
                        context,
                        AppBlockAccessibilityService::class.java
                    )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (blockType == BlockType.NIGHT) PurpleGrey80 else Yellow80,
        animationSpec = tween(500)
    )

    val buttonColor by animateColorAsState(
        targetValue = if (blockType == BlockType.NIGHT) Purple40 else Yellow60,
        animationSpec = tween(400)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    ) {
        CustomColumn(
            paddingValues = PaddingValues(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("앱 차단 허용 (접근성)", color = Color.DarkGray)
                Switch(
                    checked = isAccessibilityEnabled,
                    onCheckedChange = {
                        AccessibilityServiceChecker.requestAccessibilityPermissionIfNeeded(
                            context,
                            AppBlockAccessibilityService::class.java
                        )
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Crossfade(targetState = blockType) { type ->
                    Text(
                        text = "모드: ${if (type == BlockType.NIGHT) "취침" else "기상"}",
                        color = Color.DarkGray
                    )
                }
                Switch(
                    checked = blockType == BlockType.NIGHT,
                    onCheckedChange = {
                        if (blockType == BlockType.NIGHT) {
                            FocusBlockingManager.setBlockTypeMorning()
                        } else if (blockType == BlockType.MORNING) {
                            FocusBlockingManager.setBlockTypeNight()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            ClockText()

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = blockType, label = "") { type ->
                    Text(
                        text = if (type == BlockType.NIGHT) "자러 갈까요?" else "좋은 아침이에요!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("몇 분 동안 차단할까요?", color = Color.DarkGray)
                Spacer(Modifier.height(8.dp))
                MinutePicker(
                    selectedMinute = selectedMinute,
                    onMinuteChanged = { selectedMinute = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    blockTimeId.value = LocalDateTime.now()
                    val item = BlockTime(
                        id = blockTimeId.value.toString(),
                        minute = selectedMinute,
                        type = blockType
                    )
                    scheduler.scheduleBlock(item)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = blockType, label = "") { type ->
                        Text(
                            text = if (type == BlockType.NIGHT) "자러 갈까요?" else "좋은 아침이에요!",
                            fontSize = 20.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClockText() {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000L) // update every second
        }
    }

    Text(
        text = "현재 시간: ${currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
        fontSize = 40.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.DarkGray
    )
}