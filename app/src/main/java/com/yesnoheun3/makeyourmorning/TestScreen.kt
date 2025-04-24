package com.yesnoheun3.makeyourmorning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.compose.MinutePicker
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.accessibility.AppBlockAccessibilityService


// Preview용 위젯
@Preview
@Composable
fun TestWidget() {
    val context = LocalContext.current
    var selectedMinute by remember { mutableIntStateOf(30) }
    var isNightMode by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey80)
            .padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "취침 준비",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("앱 차단 허용 (접근성)", color = Color.DarkGray)
                Switch(
                    checked = false,
                    onCheckedChange = {
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("모드: ${if (isNightMode) "취침" else "기상"}", color = Color.DarkGray)
                Switch(
                    checked = isNightMode,
                    onCheckedChange = { isNightMode = it }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = if (isNightMode) "자러 갈까요?" else "일어날 시간이에요!",
                fontSize = 24.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold
            )

            // ⏰ 시간 선택기
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("몇 분 동안 차단할까요?", color = Color.DarkGray)
                Spacer(Modifier.height(8.dp))
                MinutePicker(
                    selectedMinute = selectedMinute,
                    onMinuteChanged = { selectedMinute = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 💤 시작 버튼
            Button(
                onClick = { /* 시작 로직 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = if (isNightMode) "누우러 가기 💤" else "일어나기 ☀️", fontSize = 20.sp)
            }
        }
    }
}
