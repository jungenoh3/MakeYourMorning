package com.yesnoheun3.makeyourmorning.utilities.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.utilities.database.dao.AlarmTimeDao

@TypeConverters(AppTypeConverter::class)
@Database(entities = [AlarmTime::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun alarmTimeDao(): AlarmTimeDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "appDB")
                        .fallbackToDestructiveMigration(false)
                        .build()
                }
            }
            return instance
        }
    }
}