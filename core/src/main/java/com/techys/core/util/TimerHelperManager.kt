package com.techys.core.util

import com.techys.core.model.TimerStateType
import com.techys.core.service.PausaService
import java.util.Timer
import java.util.TimerTask

/**
 * Based on solid principles this could be wrong since to add  a new timer type we have to change this
 * class constructor but we kinda want to keep the timer types separate either way might need improvements
 */
class TimerHelperManager(
    val eyeTimer: EyeTimerHelper,
    val focusTimer: FocusTimerHelper,
    val quickTimers: MutableList<QuickTimerHelper>
) {

    var timer: Timer? = null
    fun start() {
        startTimer()
    }

    companion object {
        const val TICK_IN_MILLIS = 1_000L
    }

    init {
        //We immediately update the pausa state to expose  the current timers to the ui layer
        PausaService.updatePausaState {
            copy(
                eyeTimer = this@TimerHelperManager.eyeTimer.getState(),
                focusTimer = this@TimerHelperManager.focusTimer.getState(),
                quickTimers = this@TimerHelperManager.quickTimers.map { quickTimer -> quickTimer.getState() }
            )
        }
    }

    fun updateTimerState(id: Int, state: TimerStateType) {
        when (id) {
            TimerConstants.EYE_TIMER_ID ->
                eyeTimer.updateTimerState(state)

            TimerConstants.FOCUS_TIMER_ID ->
                focusTimer.updateTimerState(state)

            else -> {
                quickTimers.forEach { quickTimer -> quickTimer.updateTimerState(state) }
            }
        }
    }

    fun updateTimerInfo(id: Int, title: String?, interval: Int?) {
        when (id) {
            TimerConstants.EYE_TIMER_ID ->
                eyeTimer.updateTimerInfo(title, interval)

            TimerConstants.FOCUS_TIMER_ID ->
                focusTimer.updateTimerInfo(title, interval)

            else -> {
                quickTimers.find { it.id == id }?.updateTimerInfo(title, interval)
            }
        }
        PausaService.updatePausaState {
            copy(
                eyeTimer = this@TimerHelperManager.eyeTimer.getState(),
                focusTimer = this@TimerHelperManager.focusTimer.getState(),
                quickTimers = this@TimerHelperManager.quickTimers.map { quickTimer -> quickTimer.getState() }
            )
        }
    }

    private fun startTimer() {
        if (timer != null)
            return
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    eyeTimer.onTick()
                    quickTimers.forEach { quickTimer -> quickTimer.onTick() }
                    focusTimer.onTick()
                    PausaService.updatePausaState {
                        copy(
                            eyeTimer = this@TimerHelperManager.eyeTimer.getState(),
                            focusTimer = this@TimerHelperManager.focusTimer.getState(),
                            quickTimers = this@TimerHelperManager.quickTimers.map { quickTimer -> quickTimer.getState() }
                        )
                    }
                }

            }, TICK_IN_MILLIS, TICK_IN_MILLIS)
        }
    }

    fun stop() {
        eyeTimer.updateTimerState(TimerStateType.STOPPED)
        quickTimers.forEach { quickTimer -> quickTimer.updateTimerState(TimerStateType.STOPPED) }
        focusTimer.updateTimerState(TimerStateType.STOPPED)
        timer?.let { t ->
            t.cancel()
            timer = null
        }
    }

    fun addQuickTimer(quickTimer: QuickTimerHelper) {
        quickTimers.add(quickTimer)
        PausaService.updatePausaState {
            copy(
                quickTimers = quickTimers + quickTimer.getState()
            )
        }
    }
}