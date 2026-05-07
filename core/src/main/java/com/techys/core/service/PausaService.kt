package com.techys.core.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.techys.core.notification.NotificationManager
import com.techys.core.util.EyeTimerHelper
import com.techys.core.util.FocusTimerHelper
import com.techys.core.util.ShortTimerHelper
import com.techys.core.util.TimerHelperManager

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
     * foreground notification cancellable on android 15+
     *
     * do we need this service is no timer is running?
     * make the notificaiton minimal style to contain all 3 in one notif?? -> we still need separet actions for all 3,
     *
     * define service in another android manifest with no application
     *
     * one timer in service to control all 3 features with onTick helper funciton, might sometimes start with 1 second start of 0 but that's not big deal is it?
     *
     * update notificaiton messess up chronometer!! save the orignal time we start the notificaiotn and set when??
     * scheduleAtFixedRate deprecated?!??!?!?
     *
     * we could use broadcasts for features but start service for fopregound service only but how can we
     * know if the service is already running if a feature is requested??
     *
     * keep the timer running even when the service is destroyed??
     * are we going to have multiple quick/focus timers? I dont think so, so we can keep the timer
     * classes fixed at 3
     *
     * enum into intent extras?
     *
     * foreground service notif has 3 actions for all 3 featrures, should we change the action based on timer state? or only start if not started already
     *
     * show the timer statuus/ prgressbar when the app opens on the home cards?
     *
     * differnece between val state = _state.collectAsState(_) and state by _State,collectAsState((?)???
     *
     * Use  ringtones for events??
     *
     * showing the same description text for onboarding permission requests and under settings permission items?
     *
     * believe me bro i always forget what style use for what text/component basde on material designs.... like what for settings title, body, description etc
     *
     * default tjeme color is reversed?? uisually bg is not pure white and the overlay is but the deaful theme is otherwise
     *
     * for history maybe show something like you've done x minutes of deep work (calculated with focus mode) and other similar things for other features like you took care of your eyes x times (the times eye timer has fired)
     * also what shall we show for the history? what about stats?
     * do we need graphs for the histiory section? might be useless but I think quite captivating
     * even if history and stats are for v2, we should have the database store the info for the later ui update i gues sso the user data is not lost when the feature comes or users might think we're spying on them?!?!?
     *
     * dnd mode option for focus mode??
     *
     * buttons on fg service, should it open popups- or use default values??? what if user is pro?
     */
    companion object {
        private const val SERVICE_ID = 1010
        private var eyeTimerRunning = false
        private var shortTimerRunning = false
        private var focusTimerRunning = false
        const val EYE_TIMER_KEY = "eye_timer"
        const val SHORT_TIMER_KEY = "short_timer"
        const val FOCUS_TIMER_KEY = "focus_timer"
        const val TIMER_STATE_KEY = "timer_state"
        const val TIMER_STATE_START = "start"
        const val TIMER_STATE_PAUSE = "pause"
        const val TIMER_STATE_STOP = "stop"
    }

    lateinit var timerManager: TimerHelperManager
    lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timerManager = TimerHelperManager(
            eyeTimer = EyeTimerHelper(this),
            shortTimer = ShortTimerHelper(this),
            focusTimer = FocusTimerHelper(this)
        )
        notificationManager = NotificationManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processIntent(intent)
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun processIntent(intent: Intent?) {
        intent?.let { intent ->
            val timerName = intent.action ?: return@let
            val stateId = intent.extras?.getString(TIMER_STATE_KEY) ?: return@let
            timerManager.updateTimerState(
                timerName = timerName,
                stateId = stateId
            )
        }
    }

    private fun startForeground(){
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

}
