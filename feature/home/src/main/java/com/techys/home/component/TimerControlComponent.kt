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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerStateType
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.pausa.core.R

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
                    IconButton(
                        onClick = { onRunningStateChange(TimerStateType.STARTED) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.radix_ic_play),
                            contentDescription = "",
                            tint = NeonBlue,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = { onRunningStateChange(TimerStateType.PAUSED) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.radix_ic_pause),
                            contentDescription = "",
                            tint = NeonBlue,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(Dimen.medium))
                IconButton(
                    onClick = { onRunningStateChange(TimerStateType.STOPPED) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.radix_ic_stop),
                        contentDescription = "",
                        tint = NeonBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !isTimerRunning,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
        ) {
            IconButton(
                onClick = { onRunningStateChange(TimerStateType.STARTED) },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.radix_ic_play),
                    contentDescription = "",
                    tint = NeonBlue,
                    modifier = Modifier.size(48.dp)
                )
            }
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