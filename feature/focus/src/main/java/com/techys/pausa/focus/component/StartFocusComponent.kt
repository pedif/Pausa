package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.techys.designsystem.component.PausaIconButton
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.core.R


@Composable
fun StartFocusComponent(modifier: Modifier = Modifier,
                        onTimeChanged: (Int) -> Unit = {},
                        onTimerStartedClicked:()->Unit = {}) {
    Row(
        modifier = modifier.height(Dimen.FocusActionAreaHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PausaIconButton(
            painter = painterResource(R.drawable.radix_ic_play),
            onClick = onTimerStartedClicked,
            buttonColors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
            imageDescription = stringResource(R.string.action_play_desc)
        )
        Spacer(modifier = Modifier.width(Dimen.medium))
        TimerPicker(
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