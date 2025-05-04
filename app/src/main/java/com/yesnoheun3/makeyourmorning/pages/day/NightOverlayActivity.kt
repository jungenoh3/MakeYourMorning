package com.yesnoheun3.makeyourmorning.pages.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yesnoheun3.makeyourmorning.common.compose.CustomOngoing
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.ui.theme.MakeYourMorningTheme

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