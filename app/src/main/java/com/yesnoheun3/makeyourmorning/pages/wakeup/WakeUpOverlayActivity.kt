package com.yesnoheun3.makeyourmorning.pages.wakeup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.common.compose.CustomOverlayScreen
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80

class WakeUpOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CustomOverlayScreen(
                onDismiss = { finish() },
                backgroundColor = Yellow80,
                buttonColor = Yellow60
            )
        }
    }
}