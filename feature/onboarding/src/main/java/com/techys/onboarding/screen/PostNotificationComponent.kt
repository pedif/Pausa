package com.techys.onboarding.screen

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.techys.core.permission.PermissionUtil
import com.techys.designsystem.component.PermissionHandler
import com.techys.designsystem.component.SettingsRedirectComponent
import com.techys.designsystem.theme.Dimen
import com.techys.onboarding.R
import com.techys.pausa.core.R as coreR

@Composable
fun PostNotificationComponent(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var hasNotifPermission by remember {
        mutableStateOf(PermissionUtil.hasNotificationPermission(context))
    }
    var requestNotifPermission by remember { mutableStateOf(false) }
    var requestNotifScreenIntent by remember {
        mutableStateOf(false)
    }

    var hasFullScreenIntentPermission by remember {
        mutableStateOf(PermissionUtil.hasFullScreenIntentPermission(context))
    }
    var requestFullScreenIntentScreenIntent by remember {
        mutableStateOf(false)
    }

    if (requestNotifPermission) {
        PermissionHandler(
            permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            onAllGranted = {
                hasNotifPermission = true
                requestNotifPermission = false
            },
            onDenied = {
                hasNotifPermission = false
                requestNotifPermission = false
                requestNotifScreenIntent = true
            },
            onRequested = {
                requestNotifPermission = false
            }
        )
    } else if (requestNotifScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_notification),
            onRedirectClick = {
                requestNotifScreenIntent = false
                PermissionUtil.openNotificationSettings(context)
            },
            onDismissed = { requestNotifScreenIntent = false }
        )
    }

    if (requestFullScreenIntentScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_fullscreen_intent),
            onRedirectClick = {
                requestFullScreenIntentScreenIntent = false
                PermissionUtil.openFullScreenIntentSettings(context)
            },
            onDismissed = { requestFullScreenIntentScreenIntent = false }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        InfoComponent(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            PermissionButton(
                hasPermission = hasNotifPermission,
                noPermissionText = stringResource(R.string.onboarding_action_grant_notifications),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    Log.e("tagtag","tap on notif btn $requestNotifPermission")
                    requestNotifPermission = true }
            )
            PermissionButton(
                hasPermission = hasFullScreenIntentPermission,
                modifier = Modifier
                    .fillMaxWidth(),
                noPermissionText = stringResource(R.string.onboarding_action_grant_fullscreen),
                onClick = { requestFullScreenIntentScreenIntent = true }
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasNotifPermission = PermissionUtil.hasNotificationPermission(context)
                hasFullScreenIntentPermission = PermissionUtil.hasFullScreenIntentPermission(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun InfoComponent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.onboarding_notification_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.onboarding_notification_subtitle),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(Dimen.small))
        Text(
            text = stringResource(R.string.onboarding_notification_text),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.onboarding_notification_end),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    Surface {
        PostNotificationComponent(modifier = Modifier.padding(Dimen.paddingScreen))
    }
}