package com.techys.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.designsystem.theme.TextPrimary


@Composable
fun EyeTimerComponent(
    state: TimerState,
    modifier: Modifier = Modifier,
    onStateChange: (Boolean) -> Unit = {},
    onRunningStateChange: (TimerStateType) -> Unit = {}
) {

    TimerCardComponent(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimen.large)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = " 00:21/15:00",
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(Dimen.large))
            TimerPB(
                progress = state.progress
            )

            Spacer(modifier = Modifier.height(Dimen.medium))
            Row() {
                if (state.state == TimerStateType.STARTED || state.state == TimerStateType.COOLDOWN) {
                    IconButton(
                        onClick = { onRunningStateChange(TimerStateType.PAUSED) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "",
                            tint = NeonBlue,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = { onRunningStateChange(TimerStateType.STARTED) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
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
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = NeonBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        EyeTimerComponent(
            state = TimerState(
                id = 0,
                state = TimerStateType.STARTED,
                current = 45,
                max = 60,
                title = "Eye Care"
            )
        )
    }
}