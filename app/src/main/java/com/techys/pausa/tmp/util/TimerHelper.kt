package com.techys.pausa.tmp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.techys.pausa.tmp.model.TimerState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.notification.NotificationManager

/**
 * Base class for all timers
 * @param id the id of a timer, we will bind a notification to this id and also handle updating its state via the id
 * @param interval = the amount of time in seconds which it takes for a timer to finish its cycle
 *
 */
abstract class TimerHelper(
    val id: Int = 0,
    var interval: Int = 0,
    var title: String = "",
    val type: TimerType,
    val context: Context,
    val notificationManager: NotificationManager
) {

    var progress: Int = 0
    var runningState = TimerStateType.STOPPED
    val notificationId
        get() = id
    val notificationTitle
        get() = title

    /**
     * COuld be an edge case where the state is set but the timer/notif is not the same state as ??
     */
    fun updateTimerState(newState: TimerStateType) {
        if (newState == TimerStateType.STARTED)
            updateNotification(updateStartTime = true)
        if (newState == TimerStateType.STOPPED) {
            progress = 0
            cancelNotification()
        }
        if (runningState == newState)
            return
        onTimerStarted()
        runningState = newState
    }

    fun updateTimerInfo(newTitle: String?, newInterval: Int?) {
        if (newTitle != null)
            title = newTitle
        if (newInterval != null)
            interval = newInterval
        updateNotification(false)
    }

    private fun updateNotification(updateStartTime: Boolean = false) {
        val builder = notificationManager.setupTimerNotification(notificationTitle)
        builder.setProgress(interval, progress, false)
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

    protected fun cancelNotification() {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    /**
     * The event to be executed when the timer is up
     */
    abstract fun onTimeUp()

    /**
     * The event to be execute when the timer has just received the start command
     */
    abstract fun onTimerStarted()

    /**
     * executes on each tick of the timer
     */
    fun onTick() {
        if (runningState != TimerStateType.STARTED
            && runningState != TimerStateType.COOLDOWN
        )
            return
        progress++
        if (progress <= interval)
            updateNotification()
        if (progress > interval)
            onTimeUp()
    }

    fun getState(): TimerState {
        return TimerState(
            id = id,
            title = title,
            state = runningState,
            current = progress,
            max = interval
        )
    }
}