package com.techys.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    vararg permissions: String,
    onAllGranted: () -> Unit,
    onDenied: (shouldShowRationale: Boolean) -> Unit = {},
    onRequested: ()-> Unit = {}
) {

    var hasRequestedBefore = remember { false }
    val permissionState = rememberMultiplePermissionsState(permissions.toList())
    when {
        permissionState.allPermissionsGranted -> {
            onAllGranted()
        }

        !hasRequestedBefore || permissionState.shouldShowRationale -> {
            onRequested()
            hasRequestedBefore = true
            LaunchedEffect(Unit) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        else -> {
            onDenied(false)
        }
    }
}