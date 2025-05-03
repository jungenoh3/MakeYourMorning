package com.yesnoheun3.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.common.compose.CustomOverlayScreen
import com.yesnoheun3.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.yesnoheun3.makeyourmorning.ui.theme.Purple40
import com.yesnoheun3.makeyourmorning.ui.theme.PurpleGrey80

class NightOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                CustomOverlayScreen(
                    onDismiss = { finish() },
                    backgroundColor = PurpleGrey80,
                    buttonColor = Purple40
                )
            }
        }
    }
}