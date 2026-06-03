package com.techys.core.util

import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager

class QuickTimerHelper(
    notificationManager: NotificationManager,
    alarmManager: TimerAlarmManager,
    interval: Int = TimerConstants.DEFAULT_QUICK_INTERVAL,
    id: Int =TimerConstants.QUICK_TIMER_ID ,
    notificationTitle: String = "Quick Timer"
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.Quick,
        notificationManager,
        alarmManager
    ) {
    override fun onTimeUp() {
        showTimerEndNotification()
        updateTimerState(TimerStateType.STOPPED)
        cancelNotification()
    }

    override fun onTimerStarted() {}
    override fun onTimerEnded() {}

    private fun showTimerEndNotification(){}
}