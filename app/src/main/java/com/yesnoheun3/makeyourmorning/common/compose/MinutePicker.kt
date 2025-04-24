package com.yesnoheun3.makeyourmorning.common.compose

import android.widget.NumberPicker
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MinutePicker(
    selectedMinute: Int,
    onMinuteChanged: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 10
                maxValue = 120
                value = selectedMinute
                wrapSelectorWheel = true
                setOnValueChangedListener { _, _, newVal ->
                    onMinuteChanged(newVal)
                }
            }
        },
        modifier = Modifier
            .wrapContentWidth()
            .height(150.dp)
    )
}