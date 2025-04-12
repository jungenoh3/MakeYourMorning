package com.yesnoheun3.makeyourmorning.pages.time

import java.util.UUID

data class AlarmTime (
    val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val daysOfWeek: List<Int>,
    val isOn: Boolean,
    val isSleep: Boolean = true,
)
