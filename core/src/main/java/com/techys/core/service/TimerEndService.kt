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
import com.techys.core.util.TimerConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class TimerEndService() : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val SERVICE_ID = 1011
        private const val TIMER_ID_KEY = "timer_id"
        private const val TIMER_TYPE_KEY = "timer_type"
        private const val AUTO_DISMISS_DURATION = 15_000
        var isPLaying = false
        fun getTimerEndIntent(
            context: Context,
            id: Int,
            type: TimerType
        ): Intent {
            return Intent(context, TimerEndService::class.java).apply {
                putExtra(TIMER_ID_KEY, id)
                putExtra(TIMER_TYPE_KEY, type.id)
            }
        }

        private var _runningState: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val runningState: StateFlow<Boolean> = _runningState.asStateFlow()
    }

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var preferencesManager: UserPreferencesManager

    var ringtonePlayer: Ringtone? = null

    var pausaReceiver: PausaAlarmReceiver? = null

    override fun onCreate() {
        super.onCreate()
        setupReceiver()
        _runningState.value = true
    }

    private fun setupReceiver() {
        pausaReceiver = PausaAlarmReceiver {
            this.stopSelf()
        }
        pausaReceiver?.registerReceiver(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        processIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun processIntent(intent: Intent?) {
        val extras = intent?.extras ?: return
        val timerId = extras.getInt(TIMER_ID_KEY)
        val timerType =
            timerTypeIdToTimerType(extras.getString(TIMER_TYPE_KEY) ?: "")
        if (timerType == null)
            return
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
        serviceScope.launch {
            var dismissCounter = 0
            while (dismissCounter < AUTO_DISMISS_DURATION) {
                dismissCounter += 1_000
                delay(1_000)
            }
            stopSelf()
        }
    }

    private fun play(filePath: String) {
        if (isPLaying)
            return
        isPLaying = true
        val uri = if (filePath.trim().isEmpty())
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        else
            filePath.toUri()
        ringtonePlayer = RingtoneManager.getRingtone(this, uri).apply {
            play()
        }
    }

    private fun startForeground() {
        val notification = notificationManager.getTimerEndServiceNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                SERVICE_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            )
        } else {
            startForeground(SERVICE_ID, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isPLaying = false
        serviceScope.cancel()
        ringtonePlayer?.let {
            it.stop()
            ringtonePlayer = null
        }
        pausaReceiver?.let {
            unregisterReceiver(pausaReceiver)
            pausaReceiver = null
        }
        _runningState.value = false
    }
}