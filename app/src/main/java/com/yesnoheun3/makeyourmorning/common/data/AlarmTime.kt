package com.yesnoheun3.makeyourmorning.common.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity()
data class AlarmTime (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val daysOfWeek: List<Int>,
    val isOn: Boolean,
    val isSleep: Boolean = true,
)