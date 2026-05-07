package com.techys.pausa.tmp.notification

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
import com.techys.pausa.DialogActivity
import com.techys.pausa.MainActivity
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.receiver.PausaServiceReceiver
import com.techys.pausa.tmp.util.TimerConstants

class NotificationManager(val context: Context) {

    companion object {
        private const val CHANNEL_ID = "TimerService"
    }

    fun setupNotification(title: String): NotificationCompat.Builder {
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
            .setGroup("keykey")

        return notification
    }

    fun setupTimerNotification(title: String): NotificationCompat.Builder {
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
            .setWhen(System.currentTimeMillis())
            .setProgress(100, 1, false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)
            .setGroup("keykey")

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
        val builder = setupNotification(title = "Notificaiton title")
        builder.setContentText("start a timer")
        val activityIntent =
            PendingIntent.getActivities(
                context, 0, arrayOf(Intent(context, MainActivity::class.java)),
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
            getStartFocusTimerIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )
        val quickIntent = PendingIntent.getActivity(
            context,
            10,
            getStartQuickTimerIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.addAction(R.drawable.star_on, "Eye Care", eyeCareIntent)
        builder.addAction(R.drawable.star_on, "Focus Timer", focusIntent)
        builder.addAction(R.drawable.star_on, " Quick Timer", quickIntent)
        return builder.build()
    }

    private fun getStartFocusTimerIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            action = TimerType.Focus.id
        }
    }

    private fun getStartQuickTimerIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            action = TimerType.Quick.id
        }
    }
}