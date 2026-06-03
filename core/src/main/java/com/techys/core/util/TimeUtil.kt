package com.techys.core.util

import android.text.format.DateUtils
import kotlin.math.min

object TimeUtil {
    fun getElapsedTimeLabel(timeInSeconds: Int): String {
        val hours = timeInSeconds / (60 * 60)
        val minutes = (timeInSeconds % (60 * 60)) / 60
        val seconds = timeInSeconds % 60
        return if (hours > 0) {
         "${pad2(hours)}:${pad2(minutes)}:${pad2(seconds)}"
        } else {
            "${pad2(minutes)}:${pad2(seconds)}"
        }
    }
    private fun pad2(value: Int): String = value.toString().padStart(2, '0')
}