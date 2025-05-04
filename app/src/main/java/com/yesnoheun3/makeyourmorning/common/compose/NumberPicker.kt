package com.yesnoheun3.makeyourmorning.common.compose

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NumberPicker(
    minNum: Int = 10,
    maxNum: Int = 120,
    selectedNum: Int,
    color: Color,
    onNumberChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = minNum
                maxValue = maxNum
                value = selectedNum
                wrapSelectorWheel = true
                setOnValueChangedListener { _, _, newVal ->
                   onNumberChange(newVal)
                }
                setNumberPickerTextSize(this, 25f)
            }
        },
    )
}

fun setNumberPickerTextSize(numberPicker: NumberPicker, spSize: Float) {
    for (i in 0 until numberPicker.childCount) {
        val child = numberPicker.getChildAt(i)
        if (child is EditText) {
            child.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize)
        }
    }
}