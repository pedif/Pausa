package com.techys.core.util

import android.text.format.DateUtils


fun getElapsedTimeLabel(timeInSeconds: Long): String {
    return DateUtils.formatElapsedTime(timeInSeconds)
}