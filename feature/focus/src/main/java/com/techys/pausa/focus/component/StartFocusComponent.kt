package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.pausa.core.R


@Composable
fun StartFocusComponent(modifier: Modifier = Modifier,
                        onTimeChanged: (Int) -> Unit = {},
                        onTimerStartedClicked:()->Unit = {}) {
    Row(
        modifier = modifier.height(Dimen.FocusActionAreaHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onTimerStartedClicked,
            modifier = Modifier.size(Dimen.largeIconButtonSize),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.DarkGray)
        ) {
            Icon(
               painter = painterResource(R.drawable.radix_ic_play),
                contentDescription = "",
                tint = NeonBlue,
                modifier = Modifier.size(Dimen.largeIconButtonImage)
            )
        }
        Spacer(modifier = Modifier.width(Dimen.medium))
        TimerPicker(
            modifier = Modifier,
            onTimeChanged = onTimeChanged
        )
        Spacer(modifier = Modifier.width(Dimen.small))
        Text(text = "Minutes",
            color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        StartFocusComponent {  }
    }
}