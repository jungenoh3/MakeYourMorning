package com.yesnoheun3.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.yesnoheun3.makeyourmorning.common.compose.CustomOngoing
import com.yesnoheun3.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager

class OverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                val type = FocusBlockingManager.blockType.collectAsState()
                CustomOngoing(
                    type = type.value,
                    buttonText = "돌아가기",
                    isOverlay = true,
                    onDismiss = { finish() },
                )
            }
        }
    }
}