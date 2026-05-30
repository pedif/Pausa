package com.techys.core.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import com.techys.core.notification.NotificationManager
import com.techys.pausa.core.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PausaRingService() : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Inject
    lateinit var notificationManager: NotificationManager
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        val player = MediaPlayer.create(this, R.raw.zz).apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            start()
            setOnCompletionListener {
                this@PausaRingService.stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notification = notificationManager.getPausaServiceNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                12379,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            )
        } else {
            startForeground(12379, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}