package com.techys.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.techys.core.model.TimerType
import com.techys.core.service.TimerEndService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerAlarmManager @Inject constructor(@param:ApplicationContext val context: Context) {
    fun scheduleAlarm(id: Int, alarmTime: Long, type: TimerType) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmMgr.canScheduleExactAlarms().not())
                return
        }
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                id,
                TimerEndService.getTimerEndIntent(context, id, type),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context,
                id,
                TimerEndService.getTimerEndIntent(context, id, type),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, intent)
    }

    fun cancelAlarm(id: Int, type: TimerType) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                id,
                TimerEndService.getTimerEndIntent(context, id, type),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context,
                id,
                TimerEndService.getTimerEndIntent(context, id, type),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmMgr.canScheduleExactAlarms().not())
                return
        }
        alarmMgr.cancel(intent)
    }
}
