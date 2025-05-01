package com.yesnoheun3.makeyourmorning.utilities.database

import android.app.Application
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.pages.setting.data.InstalledApp
import com.yesnoheun3.makeyourmorning.utilities.database.dao.AlarmTimeDao
import com.yesnoheun3.makeyourmorning.utilities.database.dao.InstalledAppDao
import kotlinx.coroutines.flow.Flow

class AppRepository(application: Application) {
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val alarmTimeDao: AlarmTimeDao = appDatabase.alarmTimeDao()
    private val installedAppDao: InstalledAppDao = appDatabase.installedAppDao()

    fun getAllAlarmTime(): Flow<List<AlarmTime>> = alarmTimeDao.getAll()

    // expose suspend versions of these DAO methods so they can be used in viewModelScope.
    suspend fun getOneAlarmTime(id: String) = alarmTimeDao.getOne(id)

    suspend fun insertAlarmTime(item: AlarmTime) = alarmTimeDao.insertOne(item)

    suspend fun deleteAlarmTime(item: AlarmTime) = alarmTimeDao.delete(item)

    suspend fun updateAlarmTime(item: AlarmTime) = alarmTimeDao.updateOne(item)

    suspend fun updateIsOn(id: String, isOn: Boolean) = alarmTimeDao.updateIsOn(id, isOn)

    fun getAllInstalledApp(): Flow<List<String>> = installedAppDao.getAll()
    suspend fun insertInstallApp(item: InstalledApp) = installedAppDao.insertOne(item)
    suspend fun deleteInstallApp(item: InstalledApp) = installedAppDao.delete(item)
}