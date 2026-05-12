package com.techys.core.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
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
//    fun hasBatteryPermission(): Boolean = hasPermission(Manifest.permission.Batt)


    fun openSettings(context: Context, action: String) {

        val intent = Intent(action).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun openNotificationSettings(context: Context) =
        openSettings(context, Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
}