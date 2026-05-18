package com.techys.onboarding.screen

import android.Manifest
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.techys.core.permission.PermissionUtil
import com.techys.designsystem.component.PermissionHandler
import com.techys.designsystem.component.SettingsRedirectComponent
import com.techys.onboarding.R

@Composable
fun BatteryOptimizationComponent(modifier: Modifier = Modifier) {


    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.hasBatteryPermission(context))
    }
    var requestPermission by remember { mutableStateOf(false) }
    var requestScreenIntent by remember {
        mutableStateOf(false)
    }
    if (requestPermission) {
        PermissionHandler(
            permissions = arrayOf(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
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
            permissionName = "Notification",
            onRedirectClick = {
                requestScreenIntent = false
                PermissionUtil.openBatterySettings(context)
            },
            onDismissed = { requestScreenIntent = false }
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.onboarding_battery_text),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        PermissionButton(
            hasPermission = hasPermission,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = {requestPermission = true}
        )
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    Surface{
        BatteryOptimizationComponent()
    }
}