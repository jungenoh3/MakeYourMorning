package com.nochunsam.makeyourmorning.utilities.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.common.data.BlockTime
import com.nochunsam.makeyourmorning.common.data.TimeType
import com.nochunsam.makeyourmorning.utilities.accessibility.FocusBlockingManager
import java.util.Calendar

class AlarmScheduler (private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(item: AlarmTime){

        System.out.println("알림을 보냅니다")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("com.nochunsam.makeyourmorning.type", TimeType.ALARM.ordinal)
            putExtra("com.nochunsam.makeyourmorning.Id", item.id)
            putExtra("com.nochunsam.makeyourmorning.Hour", item.hour)
            putExtra("com.nochunsam.makeyourmorning.Minute", item.minute)
            putExtra("com.nochunsam.makeyourmorning.DaysOfWeek", item.daysOfWeek.toIntArray())
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
                nextAlarmTime!!.timeInMillis, // Calendar.getInstance().timeInMillis + 5 * 1000,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime!!.timeInMillis, // Calendar.getInstance().timeInMillis + 5 * 1000,
                pendingIntent
            )
        }
    }

    fun scheduleBlock(item: BlockTime){
        System.out.println("알림을 보냅니다")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("com.nochunsam.makeyourmorning.type", TimeType.BLOCK.ordinal)
            putExtra("com.nochunsam.makeyourmorning.blockType", item.type.ordinal)
            putExtra("com.nochunsam.makeyourmorning.Id", item.id)
            putExtra("com.nochunsam.makeyourmorning.Minute", item.minute)
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
                nextAlarmTime.timeInMillis, // Calendar.getInstance().timeInMillis + 1 * 15 * 1000L,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime.timeInMillis, // Calendar.getInstance().timeInMillis + 1 * 15 * 1000L,
                pendingIntent
            )
        }
        val application = context.applicationContext as Application
        FocusBlockingManager.startBlockingFor(item.minute * 60 * 1000L, item.type, application) // (분 * 초 * 1000L)
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