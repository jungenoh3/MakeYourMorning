package com.yesnoheun3.makeyourmorning.pages.time

import java.util.Calendar

data class SleepTime(
    val id: Int,
    var time: Calendar,
    var dayOfWeeks: List<Int>,
    var isOn: Boolean
)
