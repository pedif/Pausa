package com.techys.pausa.tmp.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.techys.pausa.tmp.model.PausaState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.notification.NotificationManager
import com.techys.pausa.tmp.receiver.PausaServiceReceiver
import com.techys.pausa.tmp.util.EyeTimerHelper
import com.techys.pausa.tmp.util.FocusTimerHelper
import com.techys.pausa.tmp.util.QuickTimerHelper
import com.techys.pausa.tmp.util.TimerHelperManager
import com.techys.pausa.tmp.util.TimerStarterManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
            PausaService._state.update { it.transform() }
        }
    }

    lateinit var timerManager: TimerHelperManager
    lateinit var notificationManager: NotificationManager
    var pausaReceiver: PausaServiceReceiver? = null

    lateinit var timerStarterManager: TimerStarterManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManager(this)
        timerManager = TimerHelperManager(
            eyeTimer = EyeTimerHelper(this, notificationManager),
            focusTimer = FocusTimerHelper(this, notificationManager),
            quickTimers = mutableListOf(QuickTimerHelper(this, notificationManager))
        )
        timerManager.start()
        pausaReceiver = PausaServiceReceiver(
            onTimerStateUpdateRequested = this::updateTimerState,
            onTimerInfoUpdateRequested = this::updateTimerInfo
        )
        pausaReceiver?.registerReceiver(this)
        timerStarterManager = TimerStarterManager(
            context = this,
            notificationManager = notificationManager
        )
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
        updatePausaState {
            copy(isServiceRunning = false)
        }
        pausaReceiver?.let {
            unregisterReceiver(pausaReceiver)
            pausaReceiver = null
        }
    }

}
