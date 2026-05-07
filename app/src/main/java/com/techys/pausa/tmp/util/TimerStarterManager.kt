package com.techys.pausa.tmp.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.PendingIntentCompat
import com.techys.pausa.R
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.notification.NotificationManager
import com.techys.pausa.tmp.receiver.PausaServiceReceiver

//damn it man i cannot come up with a good name for this class at all!!! the plan is to have this class to control the show and dismissal of the quick notification where users can quickly start a timer
//based on its type, I'm t hinking of having 3 separate notificaiton starters which will be available for each type whenever there is none of that type running (exception for quick timers maybe since the user can have multiple??)
// they should be always there, so we need to figure out to show it the moment user dismissed them and they have no acative notif of that timer type
class TimerStarterManager(
    val context: Context,
    val notificationManager: NotificationManager
) {

//    init {
//        Log.e("tagtag","startiong all notifs")
//        showQuickTimerNotification()
//        showEyeTimerNotification()
//        showFocusTimerNotification()
//    }
//    fun showTimerNotification(type: TimerType) {
//        when (type) {
//            TimerType.EyeBreak ->
//                showEyeTimerNotification();
//            TimerType.Focus ->
//                showFocusTimerNotification()
//
//            TimerType.Quick ->
//                showQuickTimerNotification()
//        }
//    }

//    private fun showQuickTimerNotification() {
//        val builder = notificationManager.setupNotification("start quick timer")
//        val intent = PendingIntent.getBroadcast(
//            context, 10, PausaServiceReceiver.getTimerStateUpdateBroadcast(
//                context, type = TimerType.EyeBreak, state = TimerStateType.STARTED
//            ), PendingIntent.FLAG_IMMUTABLE
//        )
//        builder.addAction(android.R.drawable.btn_plus, "5 min" , intent)
//        builder.addAction(android.R.drawable.btn_plus, "10 min" , intent)
//        builder.addAction(android.R.drawable.btn_plus, "15 min" , intent)
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            NotificationManagerCompat.from(context).notify(1235, builder.build())
//        }
//    }
//
//    private fun showFocusTimerNotification() {
//        val builder = notificationManager.setupNotification("start focus timer")
//        val intent = PendingIntent.getBroadcast(
//            context, 10, PausaServiceReceiver.getTimerStateUpdateBroadcast(
//                context, type = TimerType.EyeBreak, state = TimerStateType.STARTED
//            ), PendingIntent.FLAG_IMMUTABLE
//        )
//        builder.addAction(android.R.drawable.btn_plus, "start" , intent)
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            NotificationManagerCompat.from(context).notify(1233, builder.build())
//        }
//    }
//
//    private fun showEyeTimerNotification() {
//
//        val builder = notificationManager.setupNotification("start eye timer")
//        val intent = PendingIntent.getBroadcast(
//            context, 10, PausaServiceReceiver.getTimerStateUpdateBroadcast(
//                context, type = TimerType.EyeBreak, state = TimerStateType.STARTED
//            ), PendingIntent.FLAG_IMMUTABLE
//        )
//        builder.addAction(android.R.drawable.btn_plus, "start" , intent)
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            NotificationManagerCompat.from(context).notify(123, builder.build())
//        }
//
//    }

    fun dismissTimerNotification(type: TimerType) {

    }

}