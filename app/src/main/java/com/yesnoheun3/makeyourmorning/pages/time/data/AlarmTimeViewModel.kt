package com.yesnoheun3.makeyourmorning.pages.time.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.launch
import kotlin.math.min

class AlarmTimeViewModel(application: Application) : AndroidViewModel(application) {
    private val _repository = AppRepository(application)
    val items: LiveData<List<AlarmTime>> get() = _repository.getAllAlarmTime()

    fun getOne(id: String, onResult: (AlarmTime) -> Unit) {
        viewModelScope.launch {
            val item = _repository.getOneAlarmTime(id)
            onResult(item)
        }
    }

    fun updateIsOn(id: String, isOn: Boolean){
        viewModelScope.launch {
            _repository.updateIsOn(id, isOn)
        }
    }

    fun updateTime(origin: AlarmTime, hour: Int, minute: Int, daysOfWeek: List<Int>){
        viewModelScope.launch {
            val update = origin.copy(hour = hour, minute = minute, daysOfWeek = daysOfWeek)
            _repository.updateAlarmTime(update)
        }
    }

    fun addItem(hour: Int, minute: Int, daysOfWeek: List<Int>, isSleep: Boolean){
        viewModelScope.launch {
            val newItem = AlarmTime(
                hour = hour,
                minute = minute,
                daysOfWeek = daysOfWeek.sorted(),
                isOn = true,
                isSleep = isSleep
            )
            _repository.insertAlarmTine(newItem)
        }
    }

    fun deleteItem(item: AlarmTime){
        viewModelScope.launch {
            _repository.deleteAlarmTime(item)
        }
    }
}