package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlin.math.min

class AlarmTimeViewModel : ViewModel() {
    private  val _items = mutableStateListOf<AlarmTime>()
    val items: List<AlarmTime> get() = _items
    val last: AlarmTime get() = _items.last()

    init {
        _items.add(
            AlarmTime(
                hour = 19,
                minute = 0,
                isSleep = true,
                daysOfWeek = listOf(1, 2),
                isOn = true
            )
        )
        _items.add(
            AlarmTime(
                hour = 10,
                minute = 2,
                isSleep = false,
                daysOfWeek = listOf(1, 2),
                isOn = true
            )
        )
    }

    fun getIndex(id: String): Int {
        return _items.indexOfFirst { it.id == id }
    }

    fun updateIsOn(id: String, isOn: Boolean){
        val index = getIndex(id)
        _items[index] = _items[index].copy(isOn = isOn)
    }

    fun updateTime(id: String, hour: Int, minute: Int, daysOfWeek: List<Int>){
        val index = getIndex(id)
        _items[index] = _items[index].copy(hour = hour, minute = minute, daysOfWeek = daysOfWeek)
    }

    fun addItem(hour: Int, minute: Int, daysOfWeek: List<Int>, isSleep: Boolean){
        _items.add(AlarmTime(
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek.sorted(),
            isOn = true,
            isSleep = isSleep
        ))
    }

    fun deleteItem(item: AlarmTime){
        _items.remove(item)
    }
}