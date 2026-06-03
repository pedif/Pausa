package com.techys.core.util

import android.os.SystemClock
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import com.techys.core.util.TimerConstants.DEFAULT_EYE_COOLDOWN_INTERVAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * child class to provide the field to parent in kotlin same name error,
 * then how??
 *
 */
class EyeTimerHelper(
    notificationManager: NotificationManager,
    alarmManager: TimerAlarmManager,
    interval: Int = TimerConstants.DEFAULT_EYE_INTERVAL,
    id: Int = TimerConstants.EYE_TIMER_ID,
    notificationTitle: String = "Eye Care",
    val coolDownNotificationTitle: String = "Eye care - Take a rest"
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.EyeBreak,
        notificationManager,
        alarmManager
    ) {
    /**
     * The duration that an eye care timer takes to finish its cycle
     */
    private var timerInterval = interval
    override fun onTimeUp() {
        /**
         * We have two modes for an eye care timer we either have just finished the actual duration
         * of an eye care timer which means we need to have a cooldown duration for the user to rest their eyes
         * the other mode is that we've just finished the cooldown duration so we need to start the timer
         * with its actually duration again
         */
        if (runningState == TimerStateType.STARTED) {
            interval = DEFAULT_EYE_COOLDOWN_INTERVAL
            updateTimerState(TimerStateType.COOLDOWN)
        } else if (runningState == TimerStateType.COOLDOWN) {
            dismissTimerEndNotification()
            interval = timerInterval
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun onTimerStarted() {
    }

    override fun onTimerEnded() {
        interval = timerInterval
    }

    override fun cancelNotification() {
        super.cancelNotification()
        dismissTimerEndNotification()
    }

    private fun restartTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DEFAULT_EYE_COOLDOWN_INTERVAL.toLong() * 100)
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun updateNotification(updateStartTime: Boolean) {
        if (runningState != TimerStateType.COOLDOWN) {
            super.updateNotification(updateStartTime)
            return
        }

        SystemClock.elapsedRealtime()
        notificationManager.showTimerNotification(
            id= notificationId,
            title = coolDownNotificationTitle,
            startTime = startTime,
            progress = interval - progress,
            max = interval,
            updateStartTime
        )
    }

    private fun showTimerEndNotification(){}
    private fun dismissTimerEndNotification(){
        notificationManager.cancelNotification(TimerConstants.EYE_TIMER_END_ID)
    }
}