package com.yesnoheun3.makeyourmorning.pages.main.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val screenRoute: String,
    val icon: ImageVector
){
    object TimeSetting : BottomNavItem("시간 설정", "time", Icons.Rounded.Notifications)
    object DayManage : BottomNavItem("하루 준비", "dayManage", Icons.Rounded.Create)
    object User : BottomNavItem("설정", "user", Icons.Rounded.Person)
}
