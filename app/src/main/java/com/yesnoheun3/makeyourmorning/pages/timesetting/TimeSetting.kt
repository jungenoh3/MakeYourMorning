@file:OptIn(ExperimentalMaterial3Api::class)

package com.yesnoheun3.makeyourmorning.pages.timesetting

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yesnoheun3.makeyourmorning.common.alarmManage.AlarmItem
import com.yesnoheun3.makeyourmorning.common.alarmManage.AndroidAlarmScheduler
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone

@Composable
fun TimeSetting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val tz = TimeZone.getTimeZone(ZoneId.systemDefault())
    val currentTime = Calendar.getInstance(tz)

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    val displayTimeState = remember {
        mutableStateOf(currentTime)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker(state = timePickerState)
        Button(onClick = {
            displayTimeState.value = Calendar.getInstance(tz).apply {
                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                set(Calendar.MINUTE, timePickerState.minute)
            }
            scheduler.schedule(AlarmItem(time = displayTimeState.value))
        }) {
            Text(text = "해당 시간으로 설정!")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "다음 시간이 설정됐습니다: ${displayTimeState.value.get(Calendar.HOUR_OF_DAY)}시 ${displayTimeState.value.get(Calendar.MINUTE)}")
    }
}