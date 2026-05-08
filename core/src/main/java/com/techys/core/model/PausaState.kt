package com.techys.core.model

import com.techys.core.util.TimerConstants


/**
 * Including both staet and progerss since we probably want to show it onthe ui, otherwise we can ifnore it
 * but we for sure will need to show it for focus pomodoro timer
 *show a false/true for servoce state?? can we preserver this on service destroy? companion object on service destryoy event
 *
 * since we're going to have only one instance of eye timer and focus timer we would have only one state for them
 * however, the user might or might not want to start multiple quick timers so we should use a list to keep
 * track of all the active quick timers
 */
data class PausaState(
    val isServiceRunning: Boolean = false,
    val eyeTimer: TimerState = TimerState(id = TimerConstants.EYE_TIMER_ID),
    val focusTimer: TimerState = TimerState(id = TimerConstants.EYE_TIMER_ID),
    val quickTimers: List<TimerState> = listOf(),
)