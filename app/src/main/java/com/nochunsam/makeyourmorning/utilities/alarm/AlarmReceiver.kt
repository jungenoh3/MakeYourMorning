package com.nochunsam.makeyourmorning.utilities.alarm

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
import com.nochunsam.makeyourmorning.R
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.common.data.BlockType
import com.nochunsam.makeyourmorning.common.data.TimeType
import com.nochunsam.makeyourmorning.pages.day.DayManagerActivity
import com.nochunsam.makeyourmorning.utilities.AppForegroundTracker
import com.nochunsam.makeyourmorning.utilities.accessibility.FocusBlockingManager

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent == null || context == null){
            return
        }
        System.out.println("알림을 받았습니다.")

        val ordinal = intent.getIntExtra("com.nochunsam.makeyourmorning.type", -1)
        val timeType = if (ordinal != -1) TimeType.values()[ordinal] else null

        if (timeType == null){
            return
        }

        if (timeType == TimeType.ALARM){
            fireAlarm(context =  context, intent = intent)
        } else if (timeType == TimeType.BLOCK){
            manageBlock(context = context, intent = intent)
        }
    }

    fun fireAlarm(context: Context, intent: Intent) {
        FocusBlockingManager.setBlockTypeNight()
        val activityIntent = Intent(context, DayManagerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(activityIntent, context)
        setNextAlarm(intent, context)
    }

    fun manageBlock(context: Context, intent: Intent) {
        val blockOrdinal = intent.getIntExtra("com.nochunsam.makeyourmorning.blockType", -1)
        val blockType = if (blockOrdinal != -1) BlockType.values()[blockOrdinal] else null
        if (blockType == null){
            return
        }

        FocusBlockingManager.stopBlocking()
        if (blockType == BlockType.NIGHT){
            FocusBlockingManager.setBlockTypeMorning()
            val activityIntent = Intent(context, DayManagerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(activityIntent, context)
        } else {
            FocusBlockingManager.setBlockTypeNight()
        }
    }

    fun startActivity(activityIntent: Intent, context: Context){
        if (AppForegroundTracker.getInstance().isForeground){
            context.startActivity(activityIntent)
        } else {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else { true }
            if (hasPermission) fireNotification(context = context, activityIntent)
        }
    }

    fun setNextAlarm(intent: Intent, context: Context){
        val daysOfWeek = intent.getIntArrayExtra("com.nochunsam.makeyourmorning.DaysOfWeek")?.toList() ?: listOf()
        if (daysOfWeek.isEmpty()){
            // 여기에 해당 알림을 종료하기 -> isOn을 끄기
            return
        }
        val hour = intent.getIntExtra("com.nochunsam.makeyourmorning.Hour", 0)
        val minute = intent.getIntExtra("com.nochunsam.makeyourmorning.Minute", 0)
        val id = intent.getStringExtra("com.nochunsam.makeyourmorning.Id")

        val item = AlarmTime(
            id = id.toString(),
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek,
            isOn = true
        )
        AlarmScheduler(context).scheduleAlarm(item)
    }

    fun fireNotification(context: Context, activityIntent: Intent){
        val pendingIntent = PendingIntent.getActivity(
            context,
            1002,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "make_your_morning5134")
            .setSmallIcon(R.drawable.ic_sleep)
            .setContentTitle("잘 시간!")
            .setContentText("잘 준비를 합시다!")
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