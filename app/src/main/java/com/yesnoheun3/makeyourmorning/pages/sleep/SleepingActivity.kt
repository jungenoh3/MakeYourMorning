package com.yesnoheun3.makeyourmorning.pages.sleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class SleepingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Sleeping(onDismiss = { finish() })
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