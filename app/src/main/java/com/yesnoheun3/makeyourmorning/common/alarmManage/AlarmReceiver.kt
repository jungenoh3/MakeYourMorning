package com.yesnoheun3.makeyourmorning.common.alarmManage

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        // 서비스 시작 말고... 알림을 통해 fullScreen을 한다면?

        val serviceIntent = Intent(context, AlarmService::class.java)
        if (intent != null){
            serviceIntent.putExtras(intent)
        }

        val versionNum = Build.VERSION.SDK_INT

        if(versionNum >= Build.VERSION_CODES.S){
            val pendingIntent = PendingIntent.getForegroundService(context, 1, serviceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            pendingIntent.send()
        } else if (versionNum >= Build.VERSION_CODES.O){
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }

    }
}