package com.techys.pausa.tmp.model


//Wjhy not use a field like prgress with get method instead of defining a function like getProgress()?? the pro cons of boths
data class TimerState(
    val id: Int,
    val title: String = "",
    val state: TimerStateType = TimerStateType.STOPPED,
    val current: Int = 0,
    val max: Int = 0
) {
    /**
     * Returns a value between 0~1 which shows the current progress of the timer
     */
    val progress: Float
        get() = current.toFloat() / max.coerceAtLeast(1)

}
