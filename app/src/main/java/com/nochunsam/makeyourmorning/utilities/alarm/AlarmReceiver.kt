package com.nochunsam.makeyourmorning.utilities.alarm

import android.Manifest
import android.app.Application
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
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class NotificationItem (
    val title: String,
    val content: String,
    val icon: Int,
) {
    object Day: NotificationItem("아침이에요!", "일어나 하루를 시작하세요!", R.drawable.ic_wake)
    object Night: NotificationItem("잘 시간이에요!", "이만 하루를 마무리해봐요", R.drawable.ic_sleep)
}

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent == null || context == null){
            return
        }
        System.out.println("알림을 받았습니다.")

        val ordinal = intent.getIntExtra("com.nochunsam.makeyourmorning.type", -1)
        val timeType = if (ordinal != -1) TimeType.entries[ordinal] else null

        if (timeType == null){
            return
        }

        if (timeType == TimeType.ALARM){
            fireAlarm(context =  context, intent = intent, blockType = BlockType.NIGHT)
        } else if (timeType == TimeType.BLOCK){
            manageBlock(context = context, intent = intent)
        }
    }

    fun fireAlarm(context: Context, intent: Intent, blockType: BlockType) {
        FocusBlockingManager.setBlockTypeNight()
        val activityIntent = Intent(context, DayManagerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(activityIntent, context, blockType)
        setNextAlarm(intent, context)
    }

    fun manageBlock(context: Context, intent: Intent) {
        val blockOrdinal = intent.getIntExtra("com.nochunsam.makeyourmorning.blockType", -1)
        val blockType = if (blockOrdinal != -1) BlockType.entries[blockOrdinal] else null
        if (blockType == null){
            return
        }

        FocusBlockingManager.stopBlocking()
        if (blockType == BlockType.NIGHT){
            FocusBlockingManager.setBlockTypeMorning()
            val activityIntent = Intent(context, DayManagerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(activityIntent, context, BlockType.MORNING)
        } else {
            FocusBlockingManager.setBlockTypeNight()
        }
    }

    fun startActivity(activityIntent: Intent, context: Context, blockType: BlockType){
        if (AppForegroundTracker.getInstance().isForeground){
            context.startActivity(activityIntent)
        } else {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else { true }
            if (hasPermission) fireNotification(context = context, activityIntent = activityIntent, item = if (blockType == BlockType.NIGHT) NotificationItem.Night else NotificationItem.Day)
        }
    }

    fun setNextAlarm(intent: Intent, context: Context){
        val daysOfWeek = intent.getIntArrayExtra("com.nochunsam.makeyourmorning.DaysOfWeek")?.toList() ?: listOf()
        val id = intent.getStringExtra("com.nochunsam.makeyourmorning.Id")

        if (daysOfWeek.isEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                val repo = AppRepository(application = context.applicationContext as Application)
                repo.updateIsOn(id.toString(), false)
            }
            return
        }
        val hour = intent.getIntExtra("com.nochunsam.makeyourmorning.Hour", 0)
        val minute = intent.getIntExtra("com.nochunsam.makeyourmorning.Minute", 0)


        val item = AlarmTime(
            id = id.toString(),
            hour = hour,
            minute = minute,
            daysOfWeek = daysOfWeek,
            isOn = true
        )
        AlarmScheduler(context).scheduleAlarm(item)
    }

    fun fireNotification(context: Context, activityIntent: Intent, item: NotificationItem){
        val pendingIntent = PendingIntent.getActivity(
            context,
            1002,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "make_your_morning5134")
            .setSmallIcon(item.icon)
            .setContentTitle(item.title)
            .setContentText(item.content)
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