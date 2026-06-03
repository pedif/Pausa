package com.techys.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat

class PausaAlarmReceiver(
    private val onDismissRequested: ()->Unit
) : BroadcastReceiver() {
    companion object {
        const val ACTION_DISMISS_SERVICE = "action_dismiss"
        fun sendDismissNotification(context: Context) {
            context.sendBroadcast(getDismissNotificationIntent(context))
        }

        fun getDismissNotificationIntent(context: Context): Intent{
            val intent = Intent(ACTION_DISMISS_SERVICE)
            intent.setPackage(context.packageName)
            return intent
        }
    }

    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(ACTION_DISMISS_SERVICE)
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

       val action = intent?.action?:return
        if(action == ACTION_DISMISS_SERVICE)
            onDismissRequested()
    }

}