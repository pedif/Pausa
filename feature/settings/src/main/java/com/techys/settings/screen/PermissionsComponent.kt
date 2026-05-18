package com.techys.settings.screen

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.techys.core.permission.PermissionUtil
import com.techys.designsystem.component.NotificationPermissionHandler
import com.techys.designsystem.component.PermissionHandler
import com.techys.designsystem.component.SettingsRedirectComponent
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.settings.R

@Composable
fun PermissionsComponent(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(Dimen.medium)
        ) {
            NotificationItem()

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))

            BatteryItem()
        }
    }
}

@Composable
fun NotificationItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasNotificationPermission(context))
    }
    PermissionItem(
        permissionId = android.Manifest.permission.POST_NOTIFICATIONS,
        title = stringResource(R.string.permission_notification),
        description = stringResource(R.string.permission_notification_desc),
        hasPermission = hasPermission,
        onPermissionChanged = { hasPermission = it },
        onPermissionSettingsRequested = { PermissionUtil.openNotificationSettings(context) }
    )
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
fun BatteryItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasBatteryPermission(context))
    }
    PermissionItem(
        permissionId = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS,
        title = stringResource(R.string.permission_battery),
        description = stringResource(R.string.permission_battery),
        hasPermission = hasPermission,
        onPermissionChanged = { hasPermission = it },
        onPermissionSettingsRequested = {
            PermissionUtil.openBatterySettings(context) }
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = PermissionUtil.hasBatteryPermission(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun PermissionItem(
    permissionId: String,
    title: String,
    description: String,
    hasPermission: Boolean,
    modifier: Modifier = Modifier,
    onPermissionChanged: (Boolean) -> Unit = {},
    onPermissionSettingsRequested: () -> Unit = {}
) {
    var requestPermission by remember { mutableStateOf(false) }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    if (requestPermission) {
        PermissionHandler(
            permissions = arrayOf(permissionId),
            onAllGranted = {
                onPermissionChanged(true)
                requestPermission = false
            },
            onDenied = {
                onPermissionChanged(false)
                requestPermission = false
                requestScreenIntent = true
            }
        )
    }
    else if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = "Notification",
            onRedirectClick = {
                requestScreenIntent = false
                onPermissionSettingsRequested()
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    PermissionUi(
        title = title,
        description = description,
        hasPermission = hasPermission,
        modifier = modifier,
        onRequestPermission = { requestPermission = true }
    )
}

@Composable
private fun PermissionUi(
    title: String,
    description: String,
    hasPermission: Boolean,
    modifier: Modifier = Modifier,
    onRequestPermission: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.small)
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 50.dp)) {
            Text(text = title)
            Text(
                text = description,
                modifier = Modifier.offset(x = Dimen.small),
                style = MaterialTheme.typography.bodySmall
            )
        }
        //compose version of fill parent not max size but what other component take and this would fill
        VerticalDivider(
            modifier = Modifier
                .height(48.dp)
                .padding(vertical = Dimen.medium)
        )
        Spacer(modifier = Modifier.width(Dimen.medium))
        Switch(
            checked = hasPermission, onCheckedChange = {
                if (!hasPermission)
                    onRequestPermission()
            })
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        Surface {
            PermissionsComponent()
        }
    }
}