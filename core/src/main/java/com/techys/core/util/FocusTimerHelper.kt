package com.techys.core.util

import android.content.Context
import android.widget.Toast

class FocusTimerHelper(context: Context): TimerHelper(context) {
    override fun onTimeUp() {
        Toast.makeText(context, "on focus timer up", Toast.LENGTH_SHORT).show()
    }
}