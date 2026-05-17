package com.techys.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerStateType
import com.techys.designsystem.component.PausaIconButton
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.pausa.core.R

private val controlButtonSize = 64.dp
private val controlIconSize = 48.dp

@Composable
fun TimerControlComponent(
    state: TimerStateType,
    modifier: Modifier = Modifier,
    onRunningStateChange: (TimerStateType) -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        var isTimerRunning by remember(state) {
            mutableStateOf(
                state == TimerStateType.STARTED
                        || state == TimerStateType.COOLDOWN
                        || state == TimerStateType.PAUSED
            )
        }
        AnimatedVisibility(
            visible = isTimerRunning,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
        ) {
            Row {
                if (state == TimerStateType.PAUSED) {
                    PausaIconButton(
                        painter = painterResource(R.drawable.radix_ic_play),
                        onClick = { onRunningStateChange(TimerStateType.STARTED) },
                        imageDescription = stringResource(R.string.action_resume_desc)
                    )
                } else {
                    PausaIconButton(
                        painter = painterResource(R.drawable.radix_ic_pause),
                        onClick = { onRunningStateChange(TimerStateType.PAUSED) },
                        imageDescription = stringResource(R.string.action_pause_desc)
                    )
                }
                Spacer(modifier = Modifier.width(Dimen.medium))
                PausaIconButton(
                    painter = painterResource(R.drawable.radix_ic_stop),
                    onClick = { onRunningStateChange(TimerStateType.STOPPED) },
                    imageDescription = stringResource(R.string.action_stop_desc)
                )
            }
        }
        AnimatedVisibility(
            visible = !isTimerRunning,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
        ) {
            PausaIconButton(
                painter = painterResource(R.drawable.radix_ic_play),
                onClick = { onRunningStateChange(TimerStateType.STARTED) },
                imageDescription = stringResource(R.string.action_play_desc)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        TimerControlComponent(state = TimerStateType.STARTED)
    }
}