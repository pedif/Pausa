package com.techys.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.techys.core.service.PausaRingService

class PausaAlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { startService(it) }
    }

    private fun startService(context: Context) {
        val intent = Intent(context, PausaRingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

}