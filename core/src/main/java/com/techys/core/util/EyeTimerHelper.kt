package com.techys.core.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.contracts.contract

/**
 * child class to provide the field to parent in kotlin same name error,
 * then how??
 *
 */
class EyeTimerHelper(
    context: Context,
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
        context,
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
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "on eye timer up", Toast.LENGTH_SHORT).show()
        }
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
        } else if (runningState == TimerStateType.COOLDOWN) {
            interval = timerInterval
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun onTimerStarted() {
//        interval = DEFAULT_INTERVAL
//        progress = 0
    }

    private fun restartTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DEFAULT_COOLDOWN_INTERVAL.toLong() * 100)
            progress = 0
            updateTimerState(TimerStateType.STARTED)
        }
    }

    override fun updateNotification(updateStartTime: Boolean){
        if(runningState != TimerStateType.COOLDOWN){
            super.updateNotification(updateStartTime)
            return
        }

        val builder = notificationManager.setupTimerNotification(notificationTitle + " - Take a rest", startTime)
        builder.setProgress(interval, interval - progress, false)
        if (updateStartTime)
            builder.setWhen(System.currentTimeMillis())
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        }
    }
}