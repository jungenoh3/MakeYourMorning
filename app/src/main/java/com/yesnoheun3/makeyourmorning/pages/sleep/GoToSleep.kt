package com.yesnoheun3.makeyourmorning.pages.sleep

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.utilities.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.AppBlockAccessibilityService
import com.yesnoheun3.makeyourmorning.utilities.FocusBlockingManager
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@Composable
fun GoToSleep() {
    val isSleeping by FocusBlockingManager.isBlocking.collectAsState()

    if (isSleeping){
        SleepingTime()
    } else {
        SetSleep()
    }
}

@Composable
fun SetSleep(){
    val context = LocalContext.current
    var isAccessibilityEnabled by remember {
        mutableStateOf(
            AccessibilityServiceChecker.isAccessibilityServiceEnabled(context, AppBlockAccessibilityService::class.java)
        )
    }
    var selectedMinute by remember { mutableStateOf(30) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey80),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                FocusBlockingManager.startBlockingFor(selectedMinute * 60 * 1000L) // 30분
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

@Composable
fun SleepingTime(){
    val blockingEndTime = FocusBlockingManager.blockingEndTime
    val blockingEndDateTime = remember(blockingEndTime) {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(blockingEndTime),
            ZoneId.systemDefault()
        )
    }

    val endTime = remember { blockingEndDateTime }
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            if (currentTime == endTime){
                break
            }
            currentTime = LocalDateTime.now()
            FocusBlockingManager.checkBlocking()
            delay(1000L)
        }
    }

    val remainingDuration = Duration.between(currentTime, endTime)
    val minutesLeft = remainingDuration.toMinutes()
    val secondsLeft = remainingDuration.seconds % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey80),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "주무세요!", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text("남은 시간: ${minutesLeft}: ${secondsLeft}", fontSize = 20.sp)
        Button(onClick = {
            FocusBlockingManager.stopBlocking()
        }) {
            Text(text = "취소")
        }
    }
}