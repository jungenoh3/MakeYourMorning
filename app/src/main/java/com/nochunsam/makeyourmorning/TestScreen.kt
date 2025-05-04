package com.nochunsam.makeyourmorning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.nochunsam.makeyourmorning.ui.theme.Yellow10
import com.nochunsam.makeyourmorning.ui.theme.Yellow60
import com.nochunsam.makeyourmorning.utilities.alarm.AlarmScheduler
import java.time.LocalDateTime


class TestActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                TimeScreen()
            }
        }
    }
}

// Preview용 위젯
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeScreen(
//    popBack: () -> Unit,
//    viewModel: AlarmTimeViewModel,
//    id: String?,
//    isSleep: Boolean
) {
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)

    val currentTime = LocalDateTime.now()
    var hourText by remember { mutableStateOf(currentTime.hour.toString().padStart(2, '0')) }
    var minuteText by remember { mutableStateOf(currentTime.minute.toString().padStart(2, '0')) }




    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonSize = screenWidth / 9

    val itemState = remember { mutableStateOf<AlarmTime?>(null) }

    val selectedDays = remember { mutableStateListOf<Int>() }

//    LaunchedEffect(id) {
//        if (id != null) {
//            val item = viewModel.getOne(id)
//            itemState.value = item
//            timePickerState.apply {
//                hour = item.hour
//                minute = item.minute
//            }
//            selectedDays.addAll(item.daysOfWeek)
//        }
//    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {}){// popBack) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }

            )
        }
    ) { paddingValue ->
        CustomColumn(
            paddingValues = paddingValue
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
//                daysOfWeek.forEachIndexed { index, day ->
//                    val dayNum = daysOfWeekNum[index]
//                    val isSelected = selectedDays.contains(dayNum)
//
//                    OutlinedButton(
//                        onClick = {
//                            if (isSelected) {
//                                selectedDays.remove(dayNum)
//                            } else {
//                                selectedDays.add(dayNum)
//                            }
//                        },
//                        modifier = Modifier.size(buttonSize),
//                        shape = CircleShape,
//                        contentPadding = PaddingValues(0.dp),
//                        colors = ButtonColors(
//                            containerColor = if (isSelected) Yellow60 else Yellow80,
//                            contentColor = if (isSelected) Color.Black else Color.Gray,
//                            disabledContainerColor = Color.DarkGray,
//                            disabledContentColor = Color.White
//                        ),
//                        border = BorderStroke(1.dp, color = Yellow40)
//                    ) {
//                        Text(
//                            text = day,
//                            fontSize = 20.sp,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .wrapContentSize(Alignment.Center)
//                        )
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(32.dp))



            TextButton(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow60,
                    contentColor = Yellow10
                ),
                shape = CircleShape
            ) {
                Text(
                    "시간 추가 "
                )
            }
        }
    }
}}