package com.techys.core.util

import android.widget.Toast
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * child class to provide the field to parent in kotlin same name error,
 * then how??
 *
 */
class EyeTimerHelper(
    notificationManager: NotificationManager,
    interval: Int = TimerConstants.DEFAULT_EYE_INTERVAL,
    id: Int = TimerConstants.EYE_TIMER_ID,
    notificationTitle: String = "Eye Care",
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.EyeBreak,
        notificationManager,
    ) {
    /**
     * The duration that an eye care timer takes to finish its cycle
     */
    private var timerInterval = interval

    companion object {
        private const val DEFAULT_COOLDOWN_INTERVAL = 5
    }

    override fun onTimeUp() {
        /**
         * We have two modes for an eye care timer we either have just finished the actual duration
         * of an eye care timer which means we need to have a cooldown duration for the user to rest their eyes
         * the other mode is that we've just finished the cooldown duration so we need to start the timer
         * with its actually duration again
         */
        progress = 0
        if (runningState == TimerStateType.STARTED) {
            interval = DEFAULT_COOLDOWN_INTERVAL
            updateTimerState(TimerStateType.COOLDOWN)
            showTimerEndNotification()
        } else if (runningState == TimerStateType.COOLDOWN) {
            dismissTimerEndNotification()
            interval = timerInterval
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun onTimerStarted() {
//        interval = DEFAULT_INTERVAL
//        progress = 0
    }

    override fun onTimerEnded() {
        interval = timerInterval
        progress = 0
    }

    override fun cancelNotification() {
        super.cancelNotification()
        dismissTimerEndNotification()
    }

    private fun restartTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DEFAULT_COOLDOWN_INTERVAL.toLong() * 100)
            progress = 0
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun updateNotification(updateStartTime: Boolean) {
        if (runningState != TimerStateType.COOLDOWN) {
            super.updateNotification(updateStartTime)
            return
        }

        notificationManager.showTimerNotification(
            id= notificationId,
            title = notificationTitle + " - Take a rest",
            startTime = startTime,
            progress = interval - progress,
            max = interval,
            updateStartTime
        )
    }

    private fun showTimerEndNotification(){
        notificationManager.showEyeTimerEndNotification(TimerConstants.EYE_TIMER_END_ID, notificationTitle)
    }
    private fun dismissTimerEndNotification(){
        notificationManager.cancelNotification(TimerConstants.EYE_TIMER_END_ID)
    }
}