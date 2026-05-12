package com.techys.designsystem.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.jar.Manifest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    vararg permissions: String,
    onAllGranted:  () -> Unit,
    onDenied:  (shouldShowRationale: Boolean) -> Unit = {}
) {
    val permissionState = rememberMultiplePermissionsState(permissions.toList())
    when {
        permissionState.allPermissionsGranted -> {
            onAllGranted()
        }
        permissionState.shouldShowRationale ->{
            LaunchedEffect(Unit) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
        else -> {
                onDenied(false)
        }
    }
}

@Composable
fun NotificationPermissionHandler(
    onAllGranted:  () -> Unit,
    onDenied:  (shouldShowRationale: Boolean) -> Unit = {}
) {
    PermissionHandler(
        permissions = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
        onAllGranted = onAllGranted,
        onDenied = onDenied
    )
}