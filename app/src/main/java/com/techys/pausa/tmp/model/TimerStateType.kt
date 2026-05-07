package com.techys.pausa.tmp.model

/**
 * add extra value to the enum state??? use the enum id as extra in intent?/
 * maybe another state for finished?? usable for histroy??!?
 * So coolldown state is going to be used only for one time type, eye timer, is this the best  architecture decision here?
 */
enum class TimerStateType(val id: String) {
    STARTED("start"), PAUSED("pause"), STOPPED("stop"), COOLDOWN("cooldown");

    fun getTimerStateById(id: String){
        TimerStateType.entries.find { it.id == id }
    }
}

fun timerStateIdToTimerState(id: String): TimerStateType? {
    return TimerStateType.entries.find { it.id == id }
}