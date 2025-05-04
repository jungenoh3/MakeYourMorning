package com.nochunsam.makeyourmorning.utilities.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nochunsam.makeyourmorning.pages.setting.data.InstalledApp
import kotlinx.coroutines.flow.Flow

@Dao
interface InstalledAppDao {
    @Query("SELECT packageName FROM installedapp")
    fun getAll(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(item: InstalledApp)

    @Delete
    suspend fun delete(item: InstalledApp)
}