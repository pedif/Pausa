package com.techys.core.util

import android.content.Context
import android.widget.Toast
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuickTimerHelper(
    context: Context,
    notificationManager: NotificationManager,
    interval: Int = TimerConstants.QUICK_TIMER_ID,
    id: Int = TimerConstants.DEFAULT_QUICK_INTERVAL,
    notificationTitle: String = "Quick Timer"
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.Quick,
        context,
        notificationManager,
    ) {
    override fun onTimeUp() {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "on short timer up", Toast.LENGTH_SHORT).show()
        }
        updateTimerState(TimerStateType.STOPPED)
        cancelNotification()
    }

    override fun onTimerStarted() {}
}