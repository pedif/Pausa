package com.techys.core.util

import android.content.Context
import com.techys.core.model.TimerState
import java.util.Timer

abstract class TimerHelper(val context: Context) {
    var interval: Int = 0
    var state = TimerState.STOPPED
    private var timer: Timer? = null

    /**
     * COuld be an edge case where the state is set but the timer/notif is not the same state as ??
     */
    fun updateTimerState(newState: TimerState){
        if(state == newState)
            return

    }

    private fun showNotification(){

    }

    /**
     * The event to be executed when the timer is up
     */
    abstract fun onTimeUp()
}