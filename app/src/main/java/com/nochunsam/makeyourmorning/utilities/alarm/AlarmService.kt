package com.nochunsam.makeyourmorning.utilities.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.nochunsam.makeyourmorning.utilities.AppForegroundTracker
import com.nochunsam.makeyourmorning.R
import com.nochunsam.makeyourmorning.pages.day.NightOverlayActivity


// 사용 X 추후에 삭제 필요
class AlarmService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null){
            stopSelf()
            return START_NOT_STICKY
        }
        System.out.println("서비스가 작동합니다!")

//        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ContextCompat.checkSelfPermission(
//                applicationContext, Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//
//        if (!hasPermission) {
//            stopSelf()
//            // 뭐 여기에 알람 허가해주세요 같은거 해놓던가...
//            return START_NOT_STICKY
//        }

        val sleepIntent = Intent(this, NightOverlayActivity::class.java)
        val sleepPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(sleepIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(applicationContext, "make_your_morning5134")
            .setContentText("aaaaaaaa")
            .setContentTitle("Hello world!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .apply {
                setContentIntent(sleepPendingIntent)
            }
        val notification = builder.build()

        if (AppForegroundTracker.getInstance().isForeground){
            sleepIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(sleepIntent)
        } else {
            notificationManager.notify(1, notification)
        }


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

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        System.out.println("서비스가 종료됩니다.")
        super.onDestroy()
    }

}