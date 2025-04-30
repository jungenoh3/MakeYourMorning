package com.yesnoheun3.makeyourmorning.utilities.database

import android.app.Application
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.utilities.database.dao.AlarmTimeDao
import kotlinx.coroutines.flow.Flow

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val alarmTimeDao: AlarmTimeDao = appDatabase.alarmTimeDao()

    fun getAllAlarmTime(): Flow<List<AlarmTime>> = alarmTimeDao.getAll()

    // expose suspend versions of these DAO methods so they can be used in viewModelScope.
    suspend fun getOneAlarmTime(id: String) = alarmTimeDao.getOne(id)

    suspend fun insertAlarmTine(item: AlarmTime) = alarmTimeDao.insertOne(item)

    suspend fun deleteAlarmTime(item: AlarmTime) = alarmTimeDao.delete(item)

    suspend fun updateAlarmTime(item: AlarmTime) = alarmTimeDao.updateOne(item)

    suspend fun updateIsOn(id: String, isOn: Boolean) = alarmTimeDao.updateIsOn(id, isOn)
}