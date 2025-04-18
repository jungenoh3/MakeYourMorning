package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.alarmManage.AndroidAlarmScheduler
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTimeViewModel
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow10
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow100
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow40
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeScreen(popBack: () -> Unit, viewModel: AlarmTimeViewModel, id: String?, isSleep: Boolean){
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val daysOfWeekNum = listOf<Int>(2, 3, 4, 5, 6, 7, 1)
    val daysOfWeek = listOf<String>("월", "화", "수", "목", "금", "토", "일")

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonSize = screenWidth / 9

    val currentTime = Calendar.getInstance()

    val index = if (id != null) { viewModel.getIndex(id) } else { -1 }

    val timePickerState = rememberTimePickerState(
        initialHour = if(index > -1) { viewModel.items[index].hour }
                        else { currentTime.get(Calendar.HOUR_OF_DAY) },
        initialMinute = if(index > -1) { viewModel.items[index].minute }
                        else { currentTime.get(Calendar.MINUTE) },
        is24Hour = false,
    )

    val selectedDays = remember { if(index > -1) {
        mutableStateListOf<Int>().apply { (viewModel.items[index].daysOfWeek) }
    } else { mutableStateListOf<Int>() } }

    Column (
        modifier = Modifier.fillMaxHeight().padding(10.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEachIndexed { index, day ->
                val dayNum = daysOfWeekNum[index]
                val isSelected = selectedDays.contains(dayNum)

                OutlinedButton(
                    onClick = {
                        if (isSelected) {
                            selectedDays.remove(dayNum)
                        }
                        else {
                            selectedDays.add(dayNum)
                        }
                    },
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonColors(
                        containerColor = if (isSelected) Yellow60 else Yellow80,
                        contentColor = if (isSelected) Color.Black else Color.Gray,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, color = Yellow40)
                ) {
                    Text(
                        text = day,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        TimePicker(
            state = timePickerState,
            colors = TimePickerColors(
                clockDialColor = Yellow100,
                selectorColor = Yellow60,
                containerColor = Color.White,
                periodSelectorBorderColor = Color.Transparent,
                clockDialSelectedContentColor = Yellow10,
                clockDialUnselectedContentColor = Color.Gray,
                periodSelectorSelectedContainerColor = Yellow60,
                periodSelectorUnselectedContainerColor = Yellow100,
                periodSelectorSelectedContentColor = Yellow10,
                periodSelectorUnselectedContentColor = Color.Gray,
                timeSelectorSelectedContainerColor = Yellow60,
                timeSelectorUnselectedContainerColor = Yellow100,
                timeSelectorSelectedContentColor = Yellow10,
                timeSelectorUnselectedContentColor = Color.Gray
            )
        )

        TextButton(
            onClick = {
                if (id != null ){  // 수정할 때는 isOn에 따라서!
                    viewModel.updateTime(
                        id = id,
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                        daysOfWeek = selectedDays,
                    )
                    if (viewModel.items[index].isOn){
                        // scheduler.cancel(viewModel.items[index].id)
                        // scheduler.schedule(viewModel.items[index])
                    }
                } else { // 추가할 때는 스케쥴/
                    viewModel.addItem(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                        daysOfWeek = selectedDays,
                        isSleep = isSleep)
                    // scheduler.schedule(viewModel.last)
                }
                popBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Yellow60,
                contentColor = Yellow10
            ),
            shape = CircleShape
        ) {
            Text(text = if (id != null) { "시간 수정" } else { "시간 추가 "})
        }
    }
}