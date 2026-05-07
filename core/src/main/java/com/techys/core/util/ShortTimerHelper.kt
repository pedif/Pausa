package com.techys.core.util

import android.content.Context
import android.widget.Toast

class ShortTimerHelper(context: Context): TimerHelper(context) {
    override fun onTimeUp() {
        Toast.makeText(context, "on short timer up", Toast.LENGTH_SHORT).show()
    }
}