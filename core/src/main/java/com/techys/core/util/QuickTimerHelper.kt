package com.techys.core.util

import android.content.Context
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlin.random.Random

class QuickTimerHelper(
    notificationManager: NotificationManager,
    interval: Int = TimerConstants.QUICK_TIMER_ID,
    id: Int = TimerConstants.DEFAULT_QUICK_INTERVAL,
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

    private fun showTimerEndNotification(){

        /**
         * We probably want to show separate notifications for each quick timer end event
         */
        notificationManager.showQuickTimerEndNotification(Random.nextInt(10_000, 20_000), notificationTitle)
    }
}