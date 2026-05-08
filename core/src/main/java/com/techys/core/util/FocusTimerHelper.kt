package com.techys.core.util

import android.content.Context
import android.widget.Toast
import com.techys.core.model.TimerStateType
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusTimerHelper(
    context: Context,
    notificationManager: NotificationManager,
    interval: Int = TimerConstants.FOCUS_TIMER_ID,
    id: Int = TimerConstants.DEFAULT_FOCUS_INTERVAL,
    notificationTitle: String = "Zen Mode"
) :
    TimerHelper(
        id,
        interval,
        notificationTitle,
        TimerType.Focus,
        context,
        notificationManager,
    ) {

    override fun onTimeUp() {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "on focus timer up", Toast.LENGTH_SHORT).show()
        }
        updateTimerState(TimerStateType.STOPPED)
    }

    override fun onTimerStarted() {}
}