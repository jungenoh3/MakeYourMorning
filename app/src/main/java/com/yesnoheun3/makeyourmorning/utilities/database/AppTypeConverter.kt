package com.yesnoheun3.makeyourmorning.utilities.database

import androidx.room.TypeConverter

class AppTypeConverter {
    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String = intList.toString()
    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val result = ArrayList<Int>()
        val split = stringList.replace("[", "").replace("]", "").replace(" ", "").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (e: Exception) {

            }
        }
        return result
    }
}