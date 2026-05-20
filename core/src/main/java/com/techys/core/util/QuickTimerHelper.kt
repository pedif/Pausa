package com.techys.core.util

import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlin.random.Random

class QuickTimerHelper(
    notificationManager: NotificationManager,
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
    ) {
    override fun onTimeUp() {
        showTimerEndNotification()
        updateTimerState(TimerStateType.STOPPED)
        cancelNotification()
    }

    override fun onTimerStarted() {}
    override fun onTimerEnded() {}

    private fun showTimerEndNotification(){

        /**
         * We probably want to show separate notifications for each quick timer end event
         */
        notificationManager.showQuickTimerEndNotification(Random.nextInt(10_000, 20_000), notificationTitle, interval / 60)
    }
}