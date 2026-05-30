package com.techys.core.util

import android.util.Log
import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Base class for all timers
 * @param id the id of a timer, we will bind a notification to this id and also handle updating its state via the id
 * @param interval = the amount of time in seconds which it takes for a timer to finish its cycle
 *
 */
open class TimerHelper @Inject constructor(
    val id: Int = 0,
    var interval: Int = 0,
    var title: String = "",
    val type: TimerType,
    val notificationManager: NotificationManager,
    val alarmManager: TimerAlarmManager
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
     * Whether this timer is in its cooldown mode or not since some timers have two active states
     * a normal running state with their set interval and also a cooldown state that would reset back
     * to the original normal state after a while
     */
    var isInCooldownMode = false

    /**
     * COuld be an edge case where the state is set but the timer/notif is not the same state as ??
     */
    fun updateTimerState(newState: TimerStateType) {
        if (runningState == TimerStateType.STOPPED && newState == TimerStateType.STARTED)
            startTime = System.currentTimeMillis()

        runningState = newState
        when (newState) {
            TimerStateType.STARTED -> {
                startTimer()
                /**
                 * If we were in cooldown mode this state meant for a resume of the cooldown state
                 * and not an actual new start command for normal timer mode
                 */
                if (isInCooldownMode)
                    runningState = TimerStateType.COOLDOWN
            }

            TimerStateType.PAUSED -> {
                pauseTimer()
            }

            TimerStateType.STOPPED -> {
                stopTimer()
            }

            TimerStateType.COOLDOWN -> {
                isInCooldownMode = true
                updateNotification(updateStartTime = true)
            }
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

    private fun pauseTimer() {
        alarmManager.cancelAlarm(notificationId)
    }

    private fun stopTimer() {
        isInCooldownMode = false
        progress = 0
        cancelNotification()
        onTimerEnded()
        alarmManager.cancelAlarm(notificationId)
    }

    private fun startTimer() {
        updateNotification(updateStartTime = true)
        onTimerStarted()
        if (isInCooldownMode)
            return
        alarmManager.scheduleAlarm(
            id = notificationId,
            alarmTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((interval - progress).toLong())
        )
    }

    /**
     * The event to be executed when the timer is up
     */
    open fun onTimeUp() {}

    /**
     * The event to be execute when the timer has just received the start command
     */
    open fun onTimerStarted() {}

    open fun onTimerEnded() {}

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
        if (progress > interval) {
            isInCooldownMode = false
            onTimeUp()
        }
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