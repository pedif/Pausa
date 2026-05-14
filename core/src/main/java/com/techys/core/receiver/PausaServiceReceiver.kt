package com.techys.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.techys.core.model.TimerStateType
import com.techys.core.model.timerStateIdToTimerState

/**
 * The receiver of pausa service actions
 * @param onTimerStateUpdateRequested updates the running state of a  timer by its provided id
 * @param onTimerInfoUpdateRequested updates the title, interval of a timer by its provided id,
 * and also whether to start the timer immediately -> (id, title, interval, shouldStartImmediately)
 */
class PausaServiceReceiver(
    val onTimerStateUpdateRequested: (Int, TimerStateType) -> Unit = { _, _ -> },
    val onTimerInfoUpdateRequested: (Int, String?, Int?, Boolean) -> Unit = { _, _, _, _ -> }
) : BroadcastReceiver() {
    companion object {
        private const val ACTION_UPDATE_TIMER_STATUS_KEY = "request_update_timer_state"
        private const val ACTION_UPDATE_TIMER_INFO = "request_update_timer_info"
        private const val TIMER_ID_KEY = "timer_id"
        private const val TIMER_STATE_KEY = "timer_state"

        private const val TIMER_TITLE_KEY = "timer_key"
        private const val TIMER_INTERVAL_KEY = "timer_interval"

        private const val TIMER_START_IMMEDIATE_FLAG_KEY = "timer_start_immediately"

        fun sendTimerStateUpdateBroadcast(context: Context, id: Int, state: TimerStateType) {
            context.sendBroadcast(getTimerStateUpdateBroadcast(context, id, state))
        }

        fun getTimerStateUpdateBroadcast(context: Context, id: Int, state: TimerStateType): Intent {
            val intent = Intent(ACTION_UPDATE_TIMER_STATUS_KEY)
            intent.setPackage(context.packageName)
            intent.putExtra(TIMER_ID_KEY, id)
            intent.putExtra(TIMER_STATE_KEY, state.id)
            return intent
        }

        fun sendTimerInfoUpdateBroadcast(
            context: Context,
            id: Int,
            title: String?,
            interval: Int?,
            shouldStartImmediately: Boolean = false,
        ) {
            val intent = Intent(ACTION_UPDATE_TIMER_INFO)
            intent.setPackage(context.packageName)
            intent.putExtra(TIMER_ID_KEY, id)
            intent.putExtra(TIMER_TITLE_KEY, title)
            intent.putExtra(TIMER_INTERVAL_KEY, interval)
            intent.putExtra(TIMER_START_IMMEDIATE_FLAG_KEY, shouldStartImmediately)
            context.sendBroadcast(intent)
        }
    }

    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(ACTION_UPDATE_TIMER_STATUS_KEY)
        filter.addAction(ACTION_UPDATE_TIMER_INFO)
        ContextCompat.registerReceiver(
            context,
            this,
            filter,
            null,
            null,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        when (action) {
            ACTION_UPDATE_TIMER_STATUS_KEY -> {
                parseUpdateTimerStatusIntent(intent.extras ?: return)
            }

            ACTION_UPDATE_TIMER_INFO -> {
                parseUpdateTimerInfoIntent(intent.extras ?: return)
            }
        }
    }

    private fun parseUpdateTimerInfoIntent(extras: Bundle) {
        val id = extras.getInt(TIMER_ID_KEY)
        val title = extras.getString(TIMER_TITLE_KEY)
        val interval = extras.getInt(TIMER_INTERVAL_KEY)
        val shouldStartImmediately = extras.getBoolean(TIMER_START_IMMEDIATE_FLAG_KEY)
        //Counting 0 for interval as no interval being set
        onTimerInfoUpdateRequested(
            id,
            title,
            if (interval > 0) interval else null,
            shouldStartImmediately
        )
    }

    private fun parseUpdateTimerStatusIntent(extras: Bundle) {
        val id = extras.getInt(TIMER_ID_KEY)
        val stateId = extras.getString(TIMER_STATE_KEY) ?: return
        val state = timerStateIdToTimerState(stateId) ?: return
        onTimerStateUpdateRequested(id, state)
    }

}