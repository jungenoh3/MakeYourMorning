package com.nochunsam.makeyourmorning.common.compose

import android.util.TypedValue
import android.widget.EditText
import android.widget.NumberPicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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