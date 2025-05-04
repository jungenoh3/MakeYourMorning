package com.nochunsam.makeyourmorning.pages.time.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDaysPicker(
    selectedDays: List<Int>, onToggleDay: (Int) -> Unit, buttonSize: Dp
) {
    val daysOfWeekNum = listOf<Int>(1, 2, 3, 4, 5, 6, 7)
    val daysOfWeek = listOf<String>("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val dayNum = daysOfWeekNum[index]
            val isSelected = selectedDays.contains(dayNum)

            OutlinedButton(
                onClick = { onToggleDay(dayNum) },
                modifier = Modifier.size(buttonSize),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = outlinedButtonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = day,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}