package com.techys.core.util

import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlin.random.Random


class FocusTimerHelper(
    notificationManager: NotificationManager,
    alarmManager: TimerAlarmManager,
    interval: Int = TimerConstants.DEFAULT_FOCUS_INTERVAL,
    id: Int = TimerConstants.FOCUS_TIMER_ID,
    notificationTitle: String = "Zen Mode"
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.Focus,
        notificationManager,
        alarmManager
    ) {

    override fun onTimeUp() {
        showTimerEndNotification()
        updateTimerState(TimerStateType.STOPPED)
    }

    override fun onTimerStarted() {}
    override fun onTimerEnded() {}

    override fun updateNotification(updateStartTime: Boolean) {
        super.updateNotification(updateStartTime)
    }

    private fun showTimerEndNotification(){
        /**
         * Maybe shouw fixed id like for eye care so the notifications replace each other or we should
         * keep notificiation sof all focus events separately??
         */
        notificationManager.showFocusTimerEndNotification(Random.nextInt(10_000, 20_000), interval / 60 )
    }
}