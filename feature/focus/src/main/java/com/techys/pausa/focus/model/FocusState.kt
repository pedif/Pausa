package com.techys.pausa.focus.model

import com.techys.core.model.TimerStateType
import com.techys.core.util.TimeUtil


data class FocusState(
    val time: Int = 0,
    val timeLabel: String = TimeUtil.getElapsedTimeLabel(0),
    val progress: Float = 1.0f,
    val runningState: TimerStateType = TimerStateType.STOPPED,
)
