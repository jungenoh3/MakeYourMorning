package com.yesnoheun3.makeyourmorning.pages.sleep

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.yesnoheun3.makeyourmorning.common.alarmManage.AlarmService
import com.yesnoheun3.makeyourmorning.pages.main.NavigationGraph

class SleepActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavigationGraph("sleeping")
        }
    }
}

//@Composable
//fun OverlayCompose(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
//    val context = LocalContext.current
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color(0xFFFFFFFF)),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Overlay")
//
//        Button(onClick = {
//            onDismiss()
//        }) {
//            Text("이거 끄기")
//        }
//    }
//}