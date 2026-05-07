package com.techys.core.notification

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationManager(val context: Context) {

    val channelId = "TimerService"
    private fun setupNotification(): NotificationCompat.Builder{
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, NotificationManager.IMPORTANCE_LOW)
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context, "")
        }
        val notification: NotificationCompat.Builder = builder
            .setSmallIcon(R.drawable.btn_plus)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle("Notificaiton title")
            .setContentText("Notitication text")
            .setUsesChronometer(true)
            .setWhen(System.currentTimeMillis())
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColorized(true)

        return notification
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, importance: Int = NotificationManager.IMPORTANCE_LOW) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
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
        manager?.createNotificationChannel(notificationChannel)
    }

    fun getPausaServiceNotification(): Notification{
        return setupNotification().build()
    }
}