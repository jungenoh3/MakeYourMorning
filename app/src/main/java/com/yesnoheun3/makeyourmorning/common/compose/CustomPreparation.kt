package com.yesnoheun3.makeyourmorning.common.compose

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.yesnoheun3.makeyourmorning.ui.theme.inversePrimaryLightMediumContrast
import com.yesnoheun3.makeyourmorning.ui.theme.onSecondaryContainerLightMediumContrast
import com.yesnoheun3.makeyourmorning.ui.theme.onTertiaryDarkHighContrast
import com.yesnoheun3.makeyourmorning.ui.theme.secondaryContainerLightMediumContrast
import com.yesnoheun3.makeyourmorning.ui.theme.secondaryLightMediumContrast
import com.yesnoheun3.makeyourmorning.ui.theme.tertiaryDarkHighContrast
import com.yesnoheun3.makeyourmorning.ui.theme.tertiaryDeepDarkHighContrast
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
    blockType: BlockType = BlockType.NIGHT
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

    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground

    val buttonColor by animateColorAsState(
        targetValue = if (blockType == BlockType.NIGHT) MaterialTheme.colorScheme.primaryContainer else tertiaryDeepDarkHighContrast,
        animationSpec = tween(400)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSwitch(
                value = isAccessibilityEnabled,
                description = "앱 차단 허용",
                color = textColor,
                onCheckedChange = {
                    AccessibilityServiceChecker.requestAccessibilityPermissionIfNeeded(
                        context,
                        AppBlockAccessibilityService::class.java
                    )
                }
            )
            CustomSwitch(
                value = blockType == BlockType.NIGHT,
                description = "",
                color = textColor,
                onCheckedChange = {
                    if (blockType == BlockType.NIGHT) {
                        FocusBlockingManager.setBlockTypeMorning()
                    } else if (blockType == BlockType.MORNING) {
                        FocusBlockingManager.setBlockTypeNight()
                    }
                },
                labelContent = { checked ->
                    Crossfade(targetState = checked) { state ->
                        Text(
                            text = if (state) "취침" else "기상",
                            color = textColor,
                        )
                    }
                }
            )
        }


        ClockText(color = textColor)


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = blockType) { type ->
                    Text(
                        text = if (type == BlockType.NIGHT) "자러 갈까요?" else "좋은 아침이에요!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Text("이 시간 동안 휴대폰을 멀리하세요!", color = textColor)
        }

        NumberPicker(
            selectedNum = selectedMinute,
            onNumberChange = { selectedMinute = it },
            color = textColor
        )

        Button(
            onClick = {
//                    blockTimeId.value = LocalDateTime.now()
//                    val item = BlockTime(
//                        id = blockTimeId.value.toString(),
//                        minute = selectedMinute,
//                        type = blockType
//                    )
//                    scheduler.scheduleBlock(item)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
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
                Crossfade(targetState = blockType) { type ->
                    Text(
                        text = if (type == BlockType.NIGHT) "자러 갈까요?" else "좋은 아침이에요!",
                        fontSize = 20.sp,
                        color = if (type == BlockType.NIGHT) Color.White else Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

}

@Composable
fun CustomSwitch(
    value: Boolean,
    description: String,
    color: Color,
    onCheckedChange: () -> Unit,
    labelContent: (@Composable (Boolean) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (labelContent != null) {
            labelContent(value)
        } else {
            Text(description, color = color)
        }
        Switch(
            checked = value,
            onCheckedChange = { onCheckedChange() },
//            colors = SwitchDefaults.colors(
//                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
//                checkedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                uncheckedTrackColor = tertiaryDeepDarkHighContrast,
//                uncheckedThumbColor = MaterialTheme.colorScheme.onTertiaryContainer
//            )
        )
    }
}

@Composable
fun ClockText(color: Color) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000L) // update every second
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "현재 시간",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
        Text(
            text = "${currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
            fontSize = 60.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}