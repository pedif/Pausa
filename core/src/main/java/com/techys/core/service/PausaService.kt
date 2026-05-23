package com.techys.core.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.techys.core.model.PausaState
import com.techys.core.model.TimerStateType
import com.techys.core.notification.NotificationManager
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.EyeTimerHelper
import com.techys.core.util.FocusTimerHelper
import com.techys.core.util.QuickTimerHelper
import com.techys.core.util.TimerHelperManager
import com.techys.pausa.core.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PausaService : Service() {
    /**
     * If the service is destroyed lets save the current state of the timers and run them again????
     * expose timer state to ui??
     *
     * foreground service type
     * createa something like a bundle so features all accees to it for e.g. compose
     *
     * simple animation for phosphoore icons ?? like tick to cross and wise versa
     *
     * name compose screen part of viewpager screen component vs screen??
     *
     * Does button can have an icon and a text? like ok (thumbs up icon)
     *
     * multiple notifications for 1 foreground service??
     *
     * where to startforeground service?? on start command or oncreate? multiple onstart commands
     *
     *
     *
     */
    companion object {
        private const val SERVICE_ID = 1010

        //maybe we could make hilt inject this as singleton across app?? can we update it that way? is that better??
        private var _state = MutableStateFlow<PausaState>(PausaState())
        val state: StateFlow<PausaState> = _state.asStateFlow()
        fun updatePausaState(transform: PausaState.() -> PausaState) {
            _state.update { it.transform() }
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var collectJob: Job? = null
    lateinit var timerManager: TimerHelperManager

    @Inject
    lateinit var notificationManager: NotificationManager
    var pausaReceiver: PausaServiceReceiver? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        setupTimerManager()
        setupReceiver()
        setupTimerObserver()
    }

    private fun setupTimerObserver() {
        collectJob = serviceScope.launch {
            state.collect { pausaState ->
                val isFocusRunning = (pausaState.focusTimer.state != TimerStateType.STOPPED)
                val isEyeRunning = (pausaState.eyeTimer.state != TimerStateType.STOPPED)
                var isQuickTimerRunning = false
                pausaState.quickTimers.map { it.state }.forEach {
                    isQuickTimerRunning = isQuickTimerRunning or (it != TimerStateType.STOPPED)
                }
                if (!(isFocusRunning or isEyeRunning or isQuickTimerRunning))
                    notificationManager.hideGroupSummary()
            }
        }
    }

    private fun setupReceiver() {
        pausaReceiver = PausaServiceReceiver(
            onTimerStateUpdateRequested = this::updateTimerState,
            onTimerInfoUpdateRequested = this::updateTimerInfo
        )
        pausaReceiver?.registerReceiver(this)
    }

    private fun setupTimerManager() {
        timerManager = TimerHelperManager(
            eyeTimer = EyeTimerHelper(
                notificationManager,
                notificationTitle = resources.getString(R.string.timer_eye_default_title),
                coolDownNotificationTitle = resources.getString(R.string.timer_eye_cooldown_default_title)
            ),
            focusTimer = FocusTimerHelper(
                notificationManager,
                notificationTitle = resources.getString(R.string.timer_focus_default_title)
            ),
            quickTimers = mutableListOf(
                QuickTimerHelper(
                    notificationManager,
                    notificationTitle = resources.getString(R.string.quick_timer_default_title)
                )
            )
        )
        timerManager.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        updatePausaState {
            copy(isServiceRunning = true)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateTimerState(id: Int, state: TimerStateType) {
        timerManager.updateTimerState(
            id = id,
            state = state
        )
    }

    private fun updateTimerInfo(
        id: Int,
        title: String?,
        interval: Int?,
        shouldStartImmediately: Boolean
    ) {
        timerManager.updateTimerInfo(id, title, interval)
        if (shouldStartImmediately)
            timerManager.updateTimerState(id, TimerStateType.STARTED)
    }

    private fun startForeground() {
        val notification = notificationManager.getPausaServiceNotification()
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
        timerManager.stop()
        serviceScope.cancel()
        collectJob?.cancel()
        updatePausaState {
            copy(isServiceRunning = false)
        }
        pausaReceiver?.let {
            unregisterReceiver(pausaReceiver)
            pausaReceiver = null
        }
    }

}
