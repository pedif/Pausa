package com.techys.onboarding.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.techys.onboarding.R

@Composable
fun PostNotificationComponent(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.onboarding_post_notification_text),
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
        )
      PermissionButton(
          hasPermission = false,
          modifier =Modifier.fillMaxWidth().align(Alignment.BottomCenter))
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    Surface{
        PostNotificationComponent()
    }
}