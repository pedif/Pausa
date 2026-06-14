package com.techys.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.techys.core.model.TimerStateType
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.TimerConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.techys.core.model.TimerType
import com.techys.core.receiver.PausaAlarmReceiver
import com.techys.pausa.core.R

class NotificationManager @Inject constructor(
    @param:ApplicationContext val context: Context,
    val actionContract: NotificationActionContract,
    val timerEndContract: TimerEndActionContract
) {

    companion object {
        private const val CHANNEL_ID = "TimerService"
        private const val CHANNEL_END_TIMER_ID = "TimerEndService"
        private const val TIMER_GROUP_KEY = "groupKey"
        private const val TIMER_GROUP_ID = 99998
        private const val TIMER_END_GROUP_KEY = "EndGroupKey"
        private const val TIMER_END_GROUP_ID = 99999

        const val REQUEST_CODE_PLAY_PAUSE_TIMER = 30000
        const val REQUEST_CODE_PLAY_TIMER = 40000
        const val REQUEST_CODE_STOP_TIMER = 50000
    }

    private fun setupForegroundServiceNotification(title: String): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfo(isTimerEnd = false)
            createNotificationChannel(
                channelId = channelId,
                name = name
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.radix_ic_stopwatch)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(title)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)

        return notification
    }

    fun showTimerNotification(
        id: Int, title: String, startTime: Long, progress: Int, max: Int, updateStartTime: Boolean
    ) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfo(isTimerEnd = false)
            createNotificationChannel(
                channelId = channelId,
                name = name
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val activityIntent =
            PendingIntent.getActivities(
                context, 0, arrayOf(actionContract.getOpenAppIntent()),
                PendingIntent.FLAG_IMMUTABLE
            )
        val playIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLAY_TIMER + id,
            PausaServiceReceiver.getTimerStateUpdateBroadcast(
                context, id = id, state = TimerStateType.STARTED
            ),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pauseIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLAY_PAUSE_TIMER + id,
            PausaServiceReceiver.getTimerStateUpdateBroadcast(
                context, id = id, state = TimerStateType.PAUSED
            ),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_STOP_TIMER + id,
            PausaServiceReceiver.getTimerStateUpdateBroadcast(
                context, id = id, state = TimerStateType.STOPPED
            ),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.radix_ic_stopwatch)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(title)
            .setProgress(max, progress, false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)
            .setGroup(TIMER_GROUP_KEY)
            .setUsesChronometer(true)
            .setContentIntent(activityIntent)
            .addAction(
                R.drawable.radix_ic_stopwatch,
                context.getString(R.string.action_resume),
                playIntent
            )
            .addAction(
                R.drawable.radix_ic_stopwatch,
                context.getString(R.string.action_pause),
                pauseIntent
            )
            .addAction(
                R.drawable.radix_ic_stopwatch,
                context.getString(R.string.action_stop),
                stopIntent
            )

        if (updateStartTime) {
            showGroupSummary()
        }

        showNotification(notification.build(), id)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        name: String,
        importance: Int = NotificationManager.IMPORTANCE_LOW
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            channelId,
            name,
            importance
        )
        notificationChannel.apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(notificationChannel)
    }

    private fun getNotificationChannelInfo(isTimerEnd: Boolean = false): Pair<String, String> {
        return if (isTimerEnd) {
            Pair(
                CHANNEL_END_TIMER_ID,
                context.getString(R.string.channel_timer_end_name)
            )
        } else {
            Pair(
                CHANNEL_ID,
                context.getString(R.string.channel_pausa_name)
            )
        }
    }

    fun getPausaServiceNotification(): Notification {
        val builder =
            setupForegroundServiceNotification(title = context.getString(R.string.notification_pausa_name))
        builder.setContentText(context.getString(R.string.notification_pausa_description))
        val activityIntent =
            PendingIntent.getActivities(
                context, 0, arrayOf(actionContract.getOpenAppIntent()),
                PendingIntent.FLAG_IMMUTABLE
            )
        builder.setContentIntent(activityIntent)
        val eyeCareIntent = PendingIntent.getBroadcast(
            context,
            10,
            PausaServiceReceiver.getTimerStateUpdateBroadcast(
                context, id = TimerConstants.EYE_TIMER_ID, state = TimerStateType.STARTED
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
        val focusIntent = PendingIntent.getActivity(
            context,
            10,
            actionContract.getStartFocusTimerIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )
        val quickIntent = PendingIntent.getActivity(
            context,
            10,
            actionContract.getStartQuickTimerIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.addAction(
            android.R.drawable.ic_input_add,
            context.getString(R.string.notification_action_eye), eyeCareIntent
        )
        builder.addAction(
            android.R.drawable.ic_input_add,
            context.getString(R.string.notification_action_focus), focusIntent
        )
        builder.addAction(
            android.R.drawable.ic_input_add,
            context.getString(R.string.notification_action_quick), quickIntent
        )
        return builder.build()
    }

    fun showNotification(notification: Notification, notificationId: Int) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }

    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    fun getTimerEndNotification(
        timerType: TimerType,
        title: String,
        description: String
    ): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfo(true)
            createNotificationChannel(
                channelId = channelId,
                name = name,
                importance = NotificationManager.IMPORTANCE_HIGH
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.radix_ic_stopwatch)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(description)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setColorized(true)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setGroup(TIMER_END_GROUP_KEY)
            .setWhen(System.currentTimeMillis())
        return notification

    }

    fun showEyeTimerEndNotification(id: Int) {
        val notif = getTimerEndNotification(
            TimerType.EyeBreak,
            context.getString(R.string.timer_end_eye_title),
            description = context.getString(R.string.timer_end_eye_desc)
        )
        val intent = PendingIntent.getActivity(
            context,
            id,
            timerEndContract.getEyeTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notif.setFullScreenIntent(
            intent, true
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }

    fun showQuickTimerEndNotification(id: Int, title: String, duration: Int) {
        val notif = getTimerEndNotification(
            timerType = TimerType.Quick,
            title = context.getString(R.string.timer_end_quick_title, title),
            description = context.getString(R.string.timer_end_quick_desc, duration)
        )
        val intent = PendingIntent.getActivity(
            context,
            id,
            timerEndContract.getQuickTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notif.setFullScreenIntent(
            intent, true
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }

    fun showFocusTimerEndNotification(id: Int, duration: Int) {
        val notif = getTimerEndNotification(
            timerType = TimerType.Focus,
            title = context.getString(R.string.timer_end_focus_title),
            description = context.getString(R.string.timer_end_focus_desc, duration)
        )
        val intent = PendingIntent.getActivity(
            context,
            id,
            timerEndContract.getFocusTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notif.setFullScreenIntent(
            intent, true
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }

    private fun showGroupSummary() {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfo(false)
            createNotificationChannel(
                channelId = channelId,
                name = name,
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        builder
            .setSmallIcon(R.drawable.radix_ic_stopwatch)
            .setContentTitle(context.getString(R.string.notification_group_title))
            .setGroup(TIMER_GROUP_KEY)
            .setGroupSummary(true)  // ← THIS makes grouping work
//            .setAggregatedNotificationStyle()  // ← Android 12+ style
            .build()

        showNotification(builder.build(), TIMER_GROUP_ID)
    }

    fun hideGroupSummary() {
        cancelNotification(TIMER_GROUP_ID)
    }

    fun hideEndGroupSummary() {
        cancelNotification(TIMER_END_GROUP_ID)
    }

    fun showEyeTimerNotification(
        id: Int,
        title: String,
        startTime: Long,
        progress: Int,
        max: Int,
        updateStartTime: Boolean
    ) {
        showTimerNotification(
            id, title, startTime, progress, max, updateStartTime
        )
    }

    fun getTimerEndServiceNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfo(false)
            createNotificationChannel(
                channelId = channelId,
                name = name,
                importance = NotificationManager.IMPORTANCE_HIGH
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        builder
            .setSmallIcon(R.drawable.radix_ic_stopwatch)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(context.getString(R.string.notification_timer_end_service_name))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)

        val dismissIntent = PendingIntent.getBroadcast(
            context,
            11,
            PausaAlarmReceiver.getDismissNotificationIntent(context),
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.addAction(
            android.R.drawable.ic_delete,
            context.getString(R.string.timer_end_action_dismiss_fg), dismissIntent
        )
        return builder.build()
    }
}