package com.techys.pausa.tmp.model

/**
 * add extra value to the enum state??? use the enum id as extra in intent?/
 * maybe another state for finished?? usable for histroy??!?
 */
enum class TimerType(val id: String) {
    EyeBreak("eye"), Quick("quick"), Focus("focus");

    fun getTimerTypeById(id: String){
        TimerType.entries.find { it.id == id }
    }
}

fun timerTypeIdToTimerType(id: String): TimerType? {
    return TimerType.entries.find { it.id == id }
}