package com.nochunsam.makeyourmorning.pages.time.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmTimeViewModel(application: Application) : AndroidViewModel(application) {
    private val _repository = AppRepository(application)
    val items: StateFlow<List<AlarmTime>> = _repository
        .getAllAlarmTime()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    suspend fun getOne(id: String) : AlarmTime {
        return _repository.getOneAlarmTime(id)
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

    suspend fun addItem(hour: Int, minute: Int, daysOfWeek: List<Int>, isSleep: Boolean): AlarmTime {
        val newItem = AlarmTime(
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek.sorted(),
            isOn = true,
            isSleep = isSleep
        )
        _repository.insertAlarmTime(newItem)
        return newItem
    }

    fun deleteItem(item: AlarmTime){
        viewModelScope.launch {
            _repository.deleteAlarmTime(item)
        }
    }
}