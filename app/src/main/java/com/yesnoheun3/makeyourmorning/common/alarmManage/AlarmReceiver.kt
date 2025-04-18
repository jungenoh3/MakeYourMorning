package com.yesnoheun3.makeyourmorning.common.alarmManage

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.yesnoheun3.makeyourmorning.R
import com.yesnoheun3.makeyourmorning.pages.sleep.SleepActivity
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTime
import com.yesnoheun3.makeyourmorning.utilities.MainLifeCycleCallback
import java.util.UUID

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent == null || context == null){
            return
        }

        if (MainLifeCycleCallback.getInstance().isForeground){
            val activityIntent = Intent(context, SleepActivity::class.java)
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(activityIntent)
        } else {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
            if (hasPermission) fireNotification(context = context)
        }

        setNextAlarm(intent, context)
    }

    fun setNextAlarm(intent: Intent, context: Context){
        val daysOfWeek = intent.getIntArrayExtra("com.yesnoheun3.makeyourmorning.DaysOfWeek")?.toList() ?: listOf()
        if (daysOfWeek.isEmpty()){
            // 여기에 해당 알림을 종료하기 -> isOn을 끄기
            return
        }
        val hour = intent.getIntExtra("com.yesnoheun3.makeyourmorning.Hour", 0)
        val minute = intent.getIntExtra("com.yesnoheun3.makeyourmorning.Minute", 0)
        val id = intent.getStringExtra("com.yesnoheun3.makeyourmorning.Id")

        val item = AlarmTime(
            id = id.toString(),
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek,
            isOn = true
        )
        AndroidAlarmScheduler(context).schedule(item)
    }

    fun fireNotification(context: Context){
        val activityIntent = Intent(context, SleepActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            1002,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "make_your_morning5134")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("잘 시간!")
            .setContentText("일어나세요!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, notification)
    }
}