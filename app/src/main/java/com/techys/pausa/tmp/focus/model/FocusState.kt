package com.techys.pausa.tmp.focus.model

import com.techys.pausa.tmp.util.getElapsedTimeLabel

data class FocusState(
    val time: Int = 0,
    val timeLabel: String = getElapsedTimeLabel(0),
    val progress: Float = 1.0f
)
