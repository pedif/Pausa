package com.techys.core.notification

import android.Manifest
import android.R
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

class NotificationManager @Inject constructor(
    @param:ApplicationContext val context: Context,
    val actionContract: NotificationActionContract,
    val timerEndContract: TimerEndActionContract
) {

    companion object {
        private const val CHANNEL_ID = "TimerService"
        private const val GROUP_KEY = "groupKey"

        private const val CHANNEL_TIMER_END_ID = "TimerEnd"
    }

    private fun setupNotification(title: String): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.btn_plus)
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
            createNotificationChannel()
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.btn_plus)
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
        channelId: String = CHANNEL_ID,
        importance: Int = NotificationManager.IMPORTANCE_LOW
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            channelId,
            channelId,
            importance
        )
        notificationChannel.apply {
            lightColor = Color.BLUE
            setBypassDnd(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setAllowBubbles(true)
            }
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)
        }
        manager.createNotificationChannel(notificationChannel)
    }

    fun getPausaServiceNotification(): Notification {
        val builder = setupNotification(title = "Pausa")
        builder.setContentText("start a timer")
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
        builder.addAction(R.drawable.star_on, "Eye Care", eyeCareIntent)
        builder.addAction(R.drawable.star_on, "Zen Mode", focusIntent)
        builder.addAction(R.drawable.star_on, " Quick", quickIntent)
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

    fun getTimerEndNotification(title: String): NotificationCompat.Builder {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                CHANNEL_TIMER_END_ID,
                importance = NotificationManager.IMPORTANCE_MAX
            )
            NotificationCompat.Builder(context, CHANNEL_TIMER_END_ID)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.btn_plus)
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
        val notif = getTimerEndNotification(title)
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
        val notif = getTimerEndNotification(title)
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
        val notif = getTimerEndNotification(title)
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