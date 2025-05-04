package com.nochunsam.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.nochunsam.makeyourmorning.common.compose.CustomOngoing
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.nochunsam.makeyourmorning.utilities.accessibility.FocusBlockingManager

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