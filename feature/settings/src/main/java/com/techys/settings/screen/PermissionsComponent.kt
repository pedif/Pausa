package com.techys.settings.screen

import android.os.Build
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
import com.techys.designsystem.component.PermissionHandler
import com.techys.designsystem.component.SettingsRedirectComponent
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.settings.R
import com.techys.pausa.core.R as coreR

@Composable
fun PermissionsComponent(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(Dimen.medium)
        ) {

            Text(
                text = stringResource(R.string.permission_section_title),
                style = MaterialTheme.typography.titleMedium
            )

            NotificationItem()

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))

            AlarmItem()

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))

            // Only showing it for devices that have this permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
                FullScreenIntentItem()
                HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))
            }

            BatteryItem()
        }
    }
}

@Composable
fun FullScreenIntentItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasFullScreenIntentPermission(context))
    }

    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    var requestPermission by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = PermissionUtil.hasFullScreenIntentPermission(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_fullscreen_intent),
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openFullScreenIntentSettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    PermissionUi(
        title = stringResource(coreR.string.permission_fullscreen_intent),
        description =  stringResource(R.string.permission_fullscreen_intent_desc),
        hasPermission = hasPermission,
        modifier = modifier,
        onRequestPermission = { requestPermission = true }
    )
}

@Composable
fun AlarmItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasAlarmPermission(context))
    }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    var requestPermission by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = PermissionUtil.hasAlarmPermission(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_alarm),
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openAlarmSettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    PermissionUi(
        title = stringResource(coreR.string.permission_alarm),
        description =  stringResource(R.string.permission_alarm_desc),
        hasPermission = hasPermission,
        modifier = modifier,
        onRequestPermission = { requestPermission = true }
    )
}

@Composable
fun NotificationItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasNotificationPermission(context))
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

    var requestPermission by remember { mutableStateOf(false) }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    if (requestPermission) {
        PermissionHandler(
            permissions = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
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
    }
    else if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_notification),
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openNotificationSettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    PermissionUi(
        title = stringResource(coreR.string.permission_notification),
        description =  stringResource(R.string.permission_notification_desc),
        hasPermission = hasPermission,
        modifier = modifier,
        onRequestPermission = { requestPermission = true }
    )
}

@Composable
fun BatteryItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasBatteryPermission(context))
    }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    var requestPermission by remember { mutableStateOf(false) }

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

    if (requestScreenIntent) {
        SettingsRedirectComponent(
            permissionName = stringResource(coreR.string.permission_battery),
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openBatterySettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    PermissionUi(
        title = stringResource(coreR.string.permission_battery),
        description =  stringResource(R.string.permission_battery_desc),
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