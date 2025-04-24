package com.yesnoheun3.makeyourmorning.utilities.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.common.data.BlockTime
import com.yesnoheun3.makeyourmorning.common.data.TimeType
import com.yesnoheun3.makeyourmorning.utilities.accessibility.FocusBlockingManager
import java.util.Calendar

class AlarmScheduler (private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(item: AlarmTime){

        System.out.println("알림을 보냅니다")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("com.yesnoheun3.makeyourmorning.type", TimeType.ALARM.ordinal)
            putExtra("com.yesnoheun3.makeyourmorning.Id", item.id)
            putExtra("com.yesnoheun3.makeyourmorning.Hour", item.hour)
            putExtra("com.yesnoheun3.makeyourmorning.Minute", item.minute)
            putExtra("com.yesnoheun3.makeyourmorning.DaysOfWeek", item.daysOfWeek.toIntArray())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = Calendar.getInstance()
        var nextAlarmTime: Calendar? = null

        for (i in 0..6) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.DATE, i)
                set(Calendar.HOUR_OF_DAY, item.hour)
                set(Calendar.MINUTE, item.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

            if ((item.daysOfWeek.isEmpty() || item.daysOfWeek.contains(dayOfWeek))
                && cal.after(now)) {
                nextAlarmTime = cal
                break
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 5 * 1000, // nextAlarmTime!!.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 5 * 1000, // nextAlarmTime!!.timeInMillis,
                pendingIntent
            )
        }
    }

    fun scheduleBlock(item: BlockTime){
        System.out.println("알림을 보냅니다")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("com.yesnoheun3.makeyourmorning.type", TimeType.BLOCK.ordinal)
            putExtra("com.yesnoheun3.makeyourmorning.blockType", item.type.ordinal)
            putExtra("com.yesnoheun3.makeyourmorning.Id", item.id)
            putExtra("com.yesnoheun3.makeyourmorning.Minute", item.minute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextAlarmTime: Calendar = Calendar.getInstance()
        nextAlarmTime.add(Calendar.MINUTE, item.minute)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 1 * 15 * 1000L, // nextAlarmTime.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 1 * 15 * 1000L, // nextAlarmTime.timeInMillis,
                pendingIntent
            )
        }
        FocusBlockingManager.startBlockingFor(1 * 15 * 1000L, item.type) // (분 * 초 * 1000L)
    }

    fun cancel(id: String){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        System.out.println("알림을 취소했습니다.")
    }
}