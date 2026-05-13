package com.techys.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.core.net.toUri
import com.techys.core.model.TimerType
import com.techys.pausa.core.R

class NotificationManager @Inject constructor(
    @param:ApplicationContext val context: Context,
    val actionContract: NotificationActionContract,
    val timerEndContract: TimerEndActionContract
) {

    companion object {
        private const val CHANNEL_ID = "TimerService"
        private const val GROUP_KEY = "groupKey"

        const val CHANNEL_TIMER_END_EYE_ID = "end_eye_time"
        const val CHANNEL_TIMER_END_QUICK_ID = "end_quick_timer"
        const val CHANNEL_TIMER_END_FOCUS_ID = "end_focus_mode"
    }

    private fun setupForegroundServiceNotification(title: String): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfoByType(null)
            createNotificationChannel(
                channelId = channelId,
                name = name
            )
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
//            .setSmallIcon(R.drawable.btn_plus)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(title)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)
            .setGroup(GROUP_KEY)

        return notification
    }

    fun showTimerNotification(
        id: Int, title: String, startTime: Long, progress: Int, max: Int, updateStartTime: Boolean
    ) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfoByType(null)
            createNotificationChannel(
                channelId = channelId,
                name = name
            )
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
//            .setSmallIcon(R.drawable.btn_plus)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(title)
            .setUsesChronometer(true)
            .setProgress(max, progress, false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)
            .setGroup(GROUP_KEY)

        if (updateStartTime)
            notification.setWhen(startTime)

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
            lightColor = Color.BLUE
            setBypassDnd(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setAllowBubbles(true)
            }
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(notificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNotificationChannel(timerType: TimerType, soundUri: String? = null) {
        val (channelId, name) = getNotificationChannelInfoByType(timerType)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Delete existing
        manager.deleteNotificationChannel(channelId)

        // Create new with selected sound
        val uri = soundUri?.toUri()

        val channel =
            NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Timer notifications"
                enableVibration(true)
                setBypassDnd(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            }

        manager.createNotificationChannel(channel)
    }

    private fun getNotificationChannelInfoByType(type: TimerType?): Pair<String, String> {
        return when (type) {
            TimerType.EyeBreak -> Pair(
                CHANNEL_TIMER_END_EYE_ID,
                context.getString(R.string.channel_timer_end_eye_name)
            )

            TimerType.Quick -> Pair(
                CHANNEL_TIMER_END_QUICK_ID,
                context.getString(R.string.channel_timer_end_quick_name)
            )

            TimerType.Focus -> Pair(
                CHANNEL_TIMER_END_FOCUS_ID,
                context.getString(R.string.channel_timer_end_focus_name)
            )

            else -> {
                Pair(
                    CHANNEL_ID,
                    context.getString(R.string.channel_pausa_name)
                )
            }
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

    fun getTimerEndNotification(timerType: TimerType, title: String): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (channelId, name) = getNotificationChannelInfoByType(timerType)
            createNotificationChannel(
                channelId = channelId,
                name = name,
                importance = NotificationManager.IMPORTANCE_MAX
            )
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
//            .setSmallIcon(R.drawable.btn_plus)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle(title)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setColorized(true)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)

        return notification

    }

    fun showEyeTimerEndNotification(id: Int, title: String) {
        val notif = getTimerEndNotification(
            TimerType.EyeBreak,
            title = title
        )
        val intent = PendingIntent.getActivity(
            context,
            10,
            timerEndContract.getEyeTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE
        )
        notif.setFullScreenIntent(
            intent, false
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }

    fun showQuickTimerEndNotification(id: Int, title: String) {
        val notif = getTimerEndNotification(
            timerType = TimerType.Quick,
            title = title
        )
        val intent = PendingIntent.getActivity(
            context,
            10,
            timerEndContract.getQuickTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE
        )
        notif.setFullScreenIntent(
            intent, true
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }

    fun showFocusTimerEndNotification(id: Int, title: String) {
        val notif = getTimerEndNotification(
            timerType = TimerType.Focus,
            title = title
        )
        val intent = PendingIntent.getActivity(
            context,
            10,
            timerEndContract.getFocusTimerEndAction(),
            PendingIntent.FLAG_IMMUTABLE
        )
        notif.setFullScreenIntent(
            intent, true
        )
        notif.setContentIntent(intent)
        showNotification(notif.build(), id)
    }
}