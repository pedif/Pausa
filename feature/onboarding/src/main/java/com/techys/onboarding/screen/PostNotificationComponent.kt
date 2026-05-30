package com.techys.onboarding.screen

import android.Manifest
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

@Composable
fun PostNotificationComponent(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasNotificationPermission(context))
    }
    var requestPermission by remember { mutableStateOf(false) }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    if (requestPermission) {
        PermissionHandler(
            permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            onAllGranted = {
                hasPermission = true
                requestPermission = false
            },
            onDenied = {
                hasPermission = false
                requestPermission = false
                requestScreenIntent = true
            }
        )
    } else if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = "Notification",
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openNotificationSettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        InfoComponent(
            modifier = Modifier.fillMaxSize()
        )
        PermissionButton(
            hasPermission = hasPermission,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = { requestPermission = true }
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = PermissionUtil.hasNotificationPermission(context)
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