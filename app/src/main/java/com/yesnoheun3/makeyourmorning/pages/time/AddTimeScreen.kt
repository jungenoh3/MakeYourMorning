package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.PRIMARY_COLOR
import com.yesnoheun3.makeyourmorning.common.alarmManage.AndroidAlarmScheduler
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeScreen(popBack: () -> Unit, viewModel: AlarmTimeViewModel){
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val daysOfWeekNum = listOf<Int>(2, 3, 4, 5, 6, 7, 1)
    val daysOfWeek = listOf<String>("월", "화", "수", "목", "금", "토", "일")

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonSize = screenWidth / 9

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    val selectedDays = remember { mutableStateListOf<Int>() }

    Column (
        modifier = Modifier.fillMaxHeight().padding(10.dp),
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
                        containerColor = Color(PRIMARY_COLOR),
                        contentColor = Color.Black,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFFFFC107) else Color.Gray)
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

        TimePicker(state = timePickerState)

        TextButton(onClick = {
            // 처음에 실행할 때 추가
            viewModel.addItem(
                hour = timePickerState.hour,
                minute = timePickerState.minute,
                daysOfWeek = selectedDays)

            scheduler.schedule(viewModel.last)

            popBack()
        }) {
            Text("시간 추가")
        }
    }
}