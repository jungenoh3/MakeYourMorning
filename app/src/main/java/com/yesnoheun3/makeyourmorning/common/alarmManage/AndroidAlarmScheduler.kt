package com.yesnoheun3.makeyourmorning.common.alarmManage

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidAlarmScheduler (private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(item: AlarmItem){
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", "Sending Alarm")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10 * 1000,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10 * 1000,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    fun cancel(item: AlarmItem){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}