package com.yesnoheun3.makeyourmorning.pages.time.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yesnoheun3.makeyourmorning.common.compose.CustomColumn
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.pages.time.compose.CustomDaysPicker
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTimeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeScreen(
    popBack: () -> Unit,
    viewModel: AlarmTimeViewModel,
    id: String?,
    isSleep: Boolean
) {
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonSize = screenWidth / 9

    val currentTime = LocalDateTime.now()
    val itemState = remember { mutableStateOf<AlarmTime?>(null) }
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false
    )
    val selectedDays = remember { mutableStateListOf<Int>() }

    LaunchedEffect(id) {
        if (id != null) {
            val item = viewModel.getOne(id)
            itemState.value = item
            timePickerState.apply {
                hour = item.hour
                minute = item.minute
            }
            selectedDays.addAll(item.daysOfWeek)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }

            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ){
                CustomDaysPicker(
                    selectedDays= selectedDays,
                    onToggleDay = { day ->
                        if (selectedDays.contains(day)) selectedDays.remove(day)
                        else selectedDays.add(day)
                    },
                    buttonSize = buttonSize
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            TextButton(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (id != null) {  // 수정할 때는 isOn에 따라서!
                            viewModel.updateTime(
                                origin = itemState.value!!,
                                hour = timePickerState.hour,
                                minute = timePickerState.minute,
                                daysOfWeek = selectedDays.sorted(),
                            )
                            //                        if (item!!.isOn) {
                            //                            scheduler.cancel(item.id)
                            //                            viewModel.getOne(id) { item ->
                            //                                scheduler.scheduleAlarm(item)
                            //                            }
                            //                        }
                        } else { // 추가할 때는 스케쥴/
                            viewModel.addItem(
                                hour = timePickerState.hour,
                                minute = timePickerState.minute,
                                daysOfWeek = selectedDays.sorted(),
                                isSleep = isSleep
                            )
                            //                        val lastItem = viewModel.items.value?.lastOrNull()
                            //                        if (lastItem != null) {
                            //                            scheduler.scheduleAlarm(lastItem)
                            //                        }
                        }
                    }
                    popBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = if (id != null) {
                        "시간 수정"
                    } else {
                        "시간 추가 "
                    }
                )
            }
        }
    }
}