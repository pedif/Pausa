package com.techys.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.techys.core.notification.NotificationActionContract
import com.techys.core.notification.TimerEndActionContract
import com.techys.core.receiver.PausaAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerAlarmManager @Inject constructor(
    @param:ApplicationContext val context: Context,
    val actionContract: NotificationActionContract,
    val timerEndContract: TimerEndActionContract,
) {
    fun scheduleAlarm(id: Int, alarmTime: Long) {

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmMgr.canScheduleExactAlarms().not())
                return
        }
        val activityIntent =
            PendingIntent.getActivities(
                context, 0, arrayOf(actionContract.getOpenAppIntent()),
                PendingIntent.FLAG_IMMUTABLE
            )
        val info: AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(
            alarmTime,
            activityIntent
        )
        val intent = PendingIntent.getBroadcast(
            context,
            6259,
            Intent(context, PausaAlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmMgr.setAlarmClock(info, intent)
    }

    fun cancelAlarm(id: Int) {
        val intent = PendingIntent.getActivity(
            context,
            id,
            timerEndContract.getFocusTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmMgr.canScheduleExactAlarms().not())
                return
        }
        alarmMgr.cancel(intent)
    }
}
