package com.techys.core.model

/**
 * add extra value to the enum state??? use the enum id as extra in intent?/
 */
enum class TimerState(val id: String) {
    STARTED("start"), PAUSED("pause"), STOPPED("stop");

    fun getTimerStateById(id: String){
        TimerState.entries.find { it.id == id }
    }
}

fun stateIdToTimerState(id: String): TimerState? {
    return TimerState.entries.find { it.id == id }
}