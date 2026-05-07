package com.techys.pausa.tmp.util

import android.content.Context
import android.widget.Toast
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.notification.NotificationManager
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