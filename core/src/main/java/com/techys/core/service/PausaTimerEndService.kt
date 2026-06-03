package com.techys.core.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.net.toUri
import com.techys.core.data.UserPreferencesManager
import com.techys.core.model.TimerType
import com.techys.core.model.timerTypeIdToTimerType
import com.techys.core.notification.NotificationManager
import com.techys.core.receiver.PausaAlarmReceiver
import com.techys.core.receiver.PausaAlarmReceiver.Companion.TIMER_ID_KEY
import com.techys.core.receiver.PausaAlarmReceiver.Companion.TIMER_TYPE_KEY
import com.techys.core.util.TimerConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class PausaTimerEndService() : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        var isPLaying = false
        fun getTimerEndIntent(
            context: Context,
            id: Int,
            type: TimerType
        ): Intent {
            return Intent(context, PausaTimerEndService::class.java).apply {
                putExtra(TIMER_ID_KEY, id)
                putExtra(TIMER_TYPE_KEY, type.id)
            }
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var preferencesManager: UserPreferencesManager

    var ringtonePlayer: Ringtone? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
//        val player = MediaPlayer.create(this, R.raw.zz).apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build()
//            )
//            start()
//            setOnCompletionListener {
//                this@PausaRingService.stopSelf()
//            }
//        }

        processIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun processIntent(intent: Intent?) {
        val extras = intent?.extras ?: return
        val timerId = extras.getInt(PausaAlarmReceiver.TIMER_ID_KEY)
        val timerType =
            timerTypeIdToTimerType(extras.getString(PausaAlarmReceiver.TIMER_TYPE_KEY) ?: "")
       serviceScope.launch {
            when (timerType) {
                TimerType.EyeBreak -> {
                    notificationManager.showEyeTimerEndNotification(TimerConstants.EYE_TIMER_END_ID)
                }

                TimerType.Quick -> {
                    val timer = PausaService.state.value.quickTimers.find { it.id == timerId }
                        ?: return@launch
                    notificationManager.showQuickTimerEndNotification(
                        Random.nextInt(
                            10_000,
                            20_000
                        ),
                        timer.title,
                        timer.max / 60
                    )
                    play(preferencesManager.quickTimerEndSound.first())
                }

                TimerType.Focus -> {
                    val timer = PausaService.state.value.focusTimer
                    notificationManager.showFocusTimerEndNotification(
                        Random.nextInt(
                            10_000,
                            20_000
                        ), timer.max / 60
                    )

                    play(preferencesManager.focusTimerEndSound.first())
                }

                else -> {}
            }
        }
    }

    private fun play(filePath: String) {
        if(isPLaying)
            return
        isPLaying = true
        val uri = if (filePath.isEmpty())
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        else
            filePath.toUri()
        ringtonePlayer = RingtoneManager.getRingtone(this, uri).apply {
            play()
        }
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
        serviceScope.cancel()
    }
}