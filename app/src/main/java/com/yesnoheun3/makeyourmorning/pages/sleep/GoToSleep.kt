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
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80
import com.yesnoheun3.makeyourmorning.utilities.AccessibilityServiceChecker
import com.yesnoheun3.makeyourmorning.utilities.AppBlockAccessibilityService
import com.yesnoheun3.makeyourmorning.utilities.FocusBlockingManager


@SuppressLint("ServiceCast")
@Composable
fun GoToSleep(navController: NavController) {
    val context = LocalContext.current
    var isAccessibilityEnabled by remember {
        mutableStateOf(
            AccessibilityServiceChecker.isAccessibilityServiceEnabled(context, AppBlockAccessibilityService::class.java)
        )
    }

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
        TextButton(
            onClick = {
                FocusBlockingManager.startBlockingFor(30 * 60 * 1000L) // 30 minutes
                navController.navigate("sleeping")
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
