package com.yesnoheun3.makeyourmorning.pages.main.data

import com.yesnoheun3.makeyourmorning.R

sealed class BottomNavItem(
    val title: String,
    val screenRoute: String,
    val icon: Int
){
    object TimeSetting : BottomNavItem("시간 설정", "time", R.drawable.round_access_time_filled_24)
    object DayManage : BottomNavItem("하루 준비", "dayManage", R.drawable.round_brightness_4_24)
    object Setting : BottomNavItem("설정", "setting", R.drawable.round_settings_24)
}
