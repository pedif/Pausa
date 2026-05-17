package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
fun RunningFocusComponent(
    isTimerPaused: Boolean,
    modifier: Modifier = Modifier,
    onTimerPauseClicked: () -> Unit = {},
    onTimerStopClicked: () -> Unit = {},
    onTimerResumeClicked: () -> Unit = {}
) {

    Row(
        modifier = modifier.height(Dimen.FocusActionAreaHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isTimerPaused) {
            PausaIconButton(
                painter = painterResource(R.drawable.radix_ic_play),
                onClick = onTimerResumeClicked,
                buttonColors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                imageDescription = stringResource(R.string.action_resume_desc)
            )
        } else {
            PausaIconButton(
                painter = painterResource(R.drawable.radix_ic_pause),
                onClick = onTimerPauseClicked,
                buttonColors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                imageDescription = stringResource(R.string.action_pause_desc)
            )
        }
        Spacer(modifier = Modifier.width(Dimen.large))
        PausaIconButton(
            painter = painterResource(R.drawable.radix_ic_stop),
            onClick = onTimerStopClicked,
            buttonColors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
            imageDescription = stringResource(R.string.action_stop_desc)
        )
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme { RunningFocusComponent(isTimerPaused = false) { } }
}