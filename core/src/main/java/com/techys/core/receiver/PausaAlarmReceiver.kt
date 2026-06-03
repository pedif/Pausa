package com.techys.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.techys.core.model.TimerType
import com.techys.core.service.PausaRingService

class PausaAlarmReceiver() : BroadcastReceiver() {
    companion object {
        const val TIMER_ID_KEY = "timer_id"
        const val TIMER_TYPE_KEY = "timer_type"

        fun getTimerEndBroadcast(
            context: Context,
            id: Int,
            type: TimerType
        ): Intent {
            return Intent(context, PausaAlarmReceiver::class.java).apply {
                putExtra(TIMER_ID_KEY, id)
                putExtra(TIMER_TYPE_KEY, type.id)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras ?: return
        context?.let {
            startService(
                it,
                timerId = extras.getInt(TIMER_ID_KEY),
                timerTypeId = extras.getString(TIMER_TYPE_KEY) ?: ""
            )
        }
    }

    private fun startService(
        context: Context,
        timerId: Int,
        timerTypeId: String
    ) {
        val intent = Intent(context, PausaRingService::class.java).apply {
            putExtra(TIMER_ID_KEY, timerId)
            putExtra(TIMER_TYPE_KEY, timerTypeId)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

}