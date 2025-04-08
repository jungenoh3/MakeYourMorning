package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.Calendar

class SleepTimeModel : ViewModel() {
    private  val _items = mutableStateListOf<SleepTime>()
    val items: List<SleepTime> get() = _items

    init {
        _items.addAll(
            listOf(
                SleepTime(
                    id = 1,
                    time = Calendar.getInstance(),
                    dayOfWeeks = listOf(Calendar.MONDAY, Calendar.TUESDAY),
                    isOn = true
                ),
                SleepTime(
                    id = 2,
                    time = Calendar.getInstance(),
                    dayOfWeeks = listOf(1, 2, 3, 4, 5, 6, 7),
                    isOn = true
                ),
                SleepTime(
                    id = 3,
                    time = Calendar.getInstance(),
                    dayOfWeeks = listOf(7),
                    isOn = false
                ),
            )
        )
    }

    fun updateIsOn(index: Int, isOn: Boolean){
        _items[index] = _items[index].copy(isOn = isOn)
    }

    fun addItem(time: Calendar, daysOfWeek: List<Int>){
        _items.add(SleepTime(
            id = 5,
            time = time,
            dayOfWeeks = daysOfWeek.sorted(),
            isOn = true
        ))
    }

    fun deleteItem(item: SleepTime){
        _items.remove(item)
    }
}