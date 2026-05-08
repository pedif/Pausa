package com.techys.onboarding.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.onboarding.R

@Composable
fun PermissionButton(
    hasPermission: Boolean,
    modifier: Modifier = Modifier,
    onClick:()->Unit = {}) {

    Button(
        modifier =modifier.padding(8.dp).fillMaxWidth(),
        onClick = onClick,
        enabled = !hasPermission,
        shape = MaterialTheme.shapes.small
    ) {
        if(hasPermission){
            Text(text = stringResource(R.string.permission_granted))
        }else {
            Text(
                text = stringResource(R.string.action_request_permission)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewComponentWithPermission() {
    Surface {
        PermissionButton(
            hasPermission = true
        )
    }
}
@Preview
@Composable
private fun PreviewComponentNoPermission() {
    Surface {
        PermissionButton(
            hasPermission = false
        )
    }
}