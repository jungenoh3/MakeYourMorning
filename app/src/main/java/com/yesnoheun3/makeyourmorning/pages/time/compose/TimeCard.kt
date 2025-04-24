package com.yesnoheun3.makeyourmorning.pages.time.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Switch
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwitchDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow40
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import kotlinx.coroutines.delay
import java.util.Locale

fun stringDaysOfWeek(dayOfWeeks: List<Int>): String {
    val stringList = listOf<String>(
        "일", "월", "화", "수", "목", "금", "토"
    )

    if (dayOfWeeks.isEmpty()){
        return "오늘"
    }

    if (dayOfWeeks.size == 7){
        return "매일"
    }

    return dayOfWeeks.joinToString(separator = ", ") { stringList[it - 1] }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animateDuration: Int = 500,
    onClick: () -> Unit,
    content: @Composable (T) -> Unit
){
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmStateChange = { value ->
            if(value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animateDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animateDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = { DeleteBackground(swipeDismissState = state, onClick = onClick) },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteBackground( swipeDismissState: DismissState, onClick: () -> Unit) {
    val color = when (swipeDismissState.dismissDirection) {
        DismissDirection.EndToStart -> Color.Red
        DismissDirection.StartToEnd -> Color.Transparent
        null -> Color.Transparent
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                enabled = true,
                onClick = onClick
            )
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }

}

@Composable
fun TimeCard(data: AlarmTime, onCheckedChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                String.format(
                    Locale.KOREA, "%02d : %02d",
                    data.hour,
                    data.minute
                ),
                fontSize = 20.sp
            )
            Text(
                stringDaysOfWeek(data.daysOfWeek),
                fontSize = 15.sp
            )
        }
        Switch(
            checked = data.isOn,
            onCheckedChange = onCheckedChanged,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Yellow60,
                checkedThumbColor = Yellow40,
                uncheckedThumbColor = Color.DarkGray,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}