package com.techys.core.util

import android.content.Context
import android.widget.Toast
import kotlin.contracts.contract

/**
 * child class to provide the field to parent in kotlin same name error,
 * then how??
 *
 */
class EyeTimerHelper(context: Context): TimerHelper(context) {
    override fun onTimeUp() {
        Toast.makeText(context, "on eye timer up", Toast.LENGTH_SHORT).show()
    }
}