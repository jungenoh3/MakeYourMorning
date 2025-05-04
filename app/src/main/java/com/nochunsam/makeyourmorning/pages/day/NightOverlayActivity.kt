package com.nochunsam.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nochunsam.makeyourmorning.common.compose.CustomOngoing
import com.nochunsam.makeyourmorning.common.data.BlockType
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme

// 사용 X 추후에 삭제 필요
class NightOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                CustomOngoing(
                    type = BlockType.NIGHT,
                    buttonText = "돌아가기",
                    isOverlay = true,
                    onDismiss = { finish() },
                )
            }
        }
    }
}