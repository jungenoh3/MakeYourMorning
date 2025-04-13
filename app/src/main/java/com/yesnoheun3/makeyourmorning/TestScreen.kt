package com.yesnoheun3.makeyourmorning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow10
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow100
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow40
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import java.util.Calendar

// Preview용 위젯

@OptIn(ExperimentalMaterial3Api::class)
// @Preview
@Composable
fun TestScreen(){
    val state = rememberTimePickerState(
        initialHour = 17,
        initialMinute = 50,
        is24Hour = false
    )

    Column (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimePicker(
            state = state,
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
    }
}