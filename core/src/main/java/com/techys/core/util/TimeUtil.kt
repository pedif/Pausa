package com.techys.core.util

import android.text.format.DateUtils

object TimeUtil {
    fun getElapsedTimeLabel(timeInSeconds: Int): String {
        return DateUtils.formatElapsedTime(timeInSeconds.toLong())
    }
}