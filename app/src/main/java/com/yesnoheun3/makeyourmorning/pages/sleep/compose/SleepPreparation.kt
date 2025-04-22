package com.yesnoheun3.makeyourmorning.pages.sleep.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.compose.CustomColumn
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AppBlockAccessibilityService
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager

@Composable
fun SleepPreparation(){
    val context = LocalContext.current
    var isAccessibilityEnabled by remember {
        mutableStateOf(
            AccessibilityServiceChecker.isAccessibilityServiceEnabled(context, AppBlockAccessibilityService::class.java)
        )
    }
    var selectedMinute by remember { mutableStateOf(30) }

    CustomColumn (color = PurpleGrey80) {
        Switch(
            checked = isAccessibilityEnabled,
            onCheckedChange = {
                AccessibilityServiceChecker.requestAccessibilityPermissionIfNeeded(context,
                    AppBlockAccessibilityService::class.java)
                isAccessibilityEnabled = AccessibilityServiceChecker.isAccessibilityServiceEnabled(context, AppBlockAccessibilityService::class.java)
            }
        )
        Text(text = "자러 갈까요?", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))

        MinutePicker(
            selectedMinute = selectedMinute,
            onMinuteChanged = {selectedMinute = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = {
                FocusBlockingManager.startBlockingFor(1 * 60 * 1000L) // 30분
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40,
                contentColor = Color.Black
            ),
        ) {
            Text(text="누우러 가기",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}