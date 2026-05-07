package com.techys.core.util

import com.techys.core.model.stateIdToTimerState
import com.techys.core.service.PausaService

class TimerHelperManager(
    val eyeTimer: EyeTimerHelper,
    val shortTimer: ShortTimerHelper,
    val focusTimer: FocusTimerHelper
){

    private var eyeTimerRunning = false
    private var shortTimerRunning = false
    private var focusTimerRunning = false

    fun updateTimerState(timerName: String, stateId: String){
       val state =  stateIdToTimerState(stateId)?:return
        when(timerName){
            PausaService.EYE_TIMER_KEY ->
                eyeTimer.updateTimerState(state)

            PausaService.SHORT_TIMER_KEY ->
                shortTimer.updateTimerState(state)

            PausaService.FOCUS_TIMER_KEY ->
                focusTimer.updateTimerState(state)
        }
    }
}