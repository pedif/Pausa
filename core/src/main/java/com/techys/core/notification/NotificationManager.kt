package com.techys.core.notification

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.techys.core.model.TimerStateType
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.TimerConstants

class NotificationManager(val context: Context,
    val actionContract: NotificationActionContract) {

    companion object {
        private const val CHANNEL_ID = "TimerService"
        private const val GROUP_KEY = "groupKey"
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

    fun setupTimerNotification(title: String, startTime: Long): NotificationCompat.Builder {
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
            .setWhen(startTime)
            .setProgress(100, 1, false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)
            .setGroup(GROUP_KEY)

        return notification
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

//    private fun getStartFocusTimerIntent(): Intent {
//        return getStartFocusTimerIntent().apply {
//            action = TimerType.Focus.id
//        }
//    }
//
//    private fun getStartQuickTimerIntent(): Intent {
//        return getStartQuickTimerIntent().apply {
//            action = TimerType.Quick.id
//        }
//    }
}