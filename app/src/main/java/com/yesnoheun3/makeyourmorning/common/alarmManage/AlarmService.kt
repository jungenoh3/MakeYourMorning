package com.yesnoheun3.makeyourmorning.common.alarmManage

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class AlarmService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null){
            stopSelf()
            return START_NOT_STICKY
        }
        System.out.println("서비스가 작동합니다!")

        val channelId = "ALARM_SERVICE_CHANNEL"
        val channel = NotificationChannel(
            channelId,
            "Alarm Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        if (channel == null){
            System.out.println("채널 설정 안돼있음")
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        // Create a notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("알람")
            .setContentText("알람 내용입니다")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notification = builder.build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            } else {
                startForeground(1, notification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
            return START_NOT_STICKY
        }

        manager.notify(1, notification)


        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}