package com.nochunsam.makeyourmorning.utilities.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmTimeDao {
    @Query("SELECT * FROM alarmtime")
    fun getAll(): Flow<List<AlarmTime>>

    @Query("SELECT * FROM alarmtime WHERE id = :id")
    suspend fun getOne(id: String): AlarmTime

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(item: AlarmTime)

    @Delete
    suspend fun delete(item: AlarmTime)

    @Update
    suspend fun updateOne(item: AlarmTime)

    @Query("UPDATE alarmtime SET isOn = :isOn WHERE id = :id")
    suspend fun updateIsOn(id: String, isOn: Boolean)
}