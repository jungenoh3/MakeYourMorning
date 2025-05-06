package com.nochunsam.makeyourmorning.utilities.database

import android.app.Application
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.pages.setting.data.AllowedApp
import com.nochunsam.makeyourmorning.utilities.database.dao.AlarmTimeDao
import com.nochunsam.makeyourmorning.utilities.database.dao.AllowedAppDao
import kotlinx.coroutines.flow.Flow

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val alarmTimeDao: AlarmTimeDao = appDatabase.alarmTimeDao()
    private val allowedAppDao: AllowedAppDao = appDatabase.allowedAppDao()

    fun getAllAlarmTime(): Flow<List<AlarmTime>> = alarmTimeDao.getAll()

    // expose suspend versions of these DAO methods so they can be used in viewModelScope.
    suspend fun getOneAlarmTime(id: String) = alarmTimeDao.getOne(id)

    suspend fun insertAlarmTime(item: AlarmTime) = alarmTimeDao.insertOne(item)

    suspend fun deleteAlarmTime(item: AlarmTime) = alarmTimeDao.delete(item)

    suspend fun updateAlarmTime(item: AlarmTime) = alarmTimeDao.updateOne(item)

    suspend fun updateIsOn(id: String, isOn: Boolean) = alarmTimeDao.updateIsOn(id, isOn)

    fun getAllAllowedApp(): Flow<List<String>> = allowedAppDao.getAll()
    suspend fun insertAllowedApp(item: AllowedApp) = allowedAppDao.insertOne(item)
    suspend fun deleteAllowedApp(item: AllowedApp) = allowedAppDao.delete(item)
}