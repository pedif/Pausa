package com.techys.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.designsystem.R

@Composable
fun SettingsRedirectComponent(
    modifier: Modifier = Modifier,
    permissionName: String,
    onRedirectClick: () -> Unit = {},
    onDismissed: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissed) {
        Surface(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.paddingScreen)
            ) {
                Text(stringResource(R.string.permission_required_message))
                Spacer(modifier= Modifier.height(Dimen.large))
                Button(onClick = onRedirectClick) {
                    Text(stringResource(R.string.permission_action_open_settings))
                }
            }
        }
    }
}