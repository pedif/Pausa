package com.techys.core.permission

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.jar.Attributes
import javax.inject.Inject

object PermissionUtil {
    fun hasPermission(
        context: Context, permission: String
    ): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasNotificationPermission(
        context: Context
    ): Boolean = hasPermission(context, Manifest.permission.POST_NOTIFICATIONS)

    fun hasAlarmPermission(
        context: Context
    ): Boolean {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmMgr?.canScheduleExactAlarms() == true
        }
        return true
    }

    fun hasFullScreenIntentPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.canUseFullScreenIntent()
        } else
            true
    }


    fun openSettings(context: Context, action: String) {

        val intent = Intent(action).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun openNotificationSettings(context: Context) =
        openSettings(context, Settings.ACTION_APPLICATION_DETAILS_SETTINGS)


    fun openAlarmSettings(context: Context) =
        openSettings(context, Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)

    fun hasBatteryPermission(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isIgnoringBatteryOptimizations =
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        return isIgnoringBatteryOptimizations
    }

    fun openBatterySettings(context: Context) {
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startActivity(intent)
    }

    fun openFullScreenIntentSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val intent = Intent(
                Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT,
                "package:${context.packageName}".toUri()
            )
            context.startActivity(intent)
        }
    }
}