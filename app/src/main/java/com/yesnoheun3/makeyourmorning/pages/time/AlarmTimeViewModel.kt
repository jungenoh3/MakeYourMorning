package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class AlarmTimeViewModel : ViewModel() {
    private  val _items = mutableStateListOf<AlarmTime>()
    val items: List<AlarmTime> get() = _items
    val last: AlarmTime get() = _items.last()

    init {
        _items.clear()
    }

    fun updateIsOn(index: Int, isOn: Boolean){
        _items[index] = _items[index].copy(isOn = isOn)
    }

    fun addItem(hour: Int, minute: Int, daysOfWeek: List<Int>){
        _items.add(AlarmTime(
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek.sorted(),
            isOn = true
        ))
    }

    fun deleteItem(item: AlarmTime){
        _items.remove(item)
    }
}