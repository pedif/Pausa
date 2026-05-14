package com.techys.core.util

import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager

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
    val notificationManager: NotificationManager
) {

    var progress: Int = 0
    var runningState = TimerStateType.STOPPED
    val notificationId
        get() = id
    val notificationTitle
        get() = title

    /**
     * The time when a timer gets started
     */
    var startTime = 0L

    /**
     * COuld be an edge case where the state is set but the timer/notif is not the same state as ??
     */
    fun updateTimerState(newState: TimerStateType) {
        if (runningState == TimerStateType.STOPPED && newState == TimerStateType.STARTED)
            startTime = System.currentTimeMillis()
        if (newState == TimerStateType.STARTED || newState == TimerStateType.COOLDOWN) {
            runningState = newState
            updateNotification(updateStartTime = true)
        }
        runningState = newState
        if (newState == TimerStateType.STOPPED) {
            progress = 0
            cancelNotification()
//            onTimerEnded()
        } else if(newState == TimerStateType.STARTED){
            onTimerStarted()
        }
    }

    fun updateTimerInfo(newTitle: String?, newInterval: Int?) {
        if (newTitle != null)
            title = newTitle
        if (newInterval != null)
            interval = newInterval
        updateNotification(false)
    }

    open fun updateNotification(updateStartTime: Boolean = false) {
       notificationManager.showTimerNotification(
            id = notificationId,
            title = notificationTitle,
            startTime = startTime,
            progress = interval - progress,
            max = interval,
            updateStartTime = updateStartTime
        )
//        if (updateStartTime)
//            builder.setWhen(System.currentTimeMillis())
    }

    protected open fun cancelNotification() {
        notificationManager.cancelNotification(notificationId)
    }

    /**
     * The event to be executed when the timer is up
     */
    abstract fun onTimeUp()

    /**
     * The event to be execute when the timer has just received the start command
     */
    abstract fun onTimerStarted()

    abstract fun onTimerEnded()

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