package com.techys.pausa.tmp.util

import android.text.format.DateUtils


fun getElapsedTimeLabel(timeInSeconds: Long): String {
    return DateUtils.formatElapsedTime(timeInSeconds)
}