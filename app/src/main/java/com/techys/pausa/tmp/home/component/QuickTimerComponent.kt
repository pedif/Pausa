package com.techys.pausa.tmp.home.component

import android.graphics.pdf.models.ListItem
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.techys.pausa.tmp.model.TimerState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.ui.theme.AppTheme
import com.techys.pausa.ui.theme.Dimen
import com.techys.pausa.ui.theme.NeonBlue
import com.techys.pausa.ui.theme.TextPrimary

@Composable
fun QuickTimerComponent(
    stateList: List<TimerState>,
    modifier: Modifier = Modifier,
    onStateChange: (Int, Boolean) -> Unit = { _, _ -> },
    onRunningStateChange: (Int, TimerStateType) -> Unit = { _, _ -> }
) {

    TimerCardComponent(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                stateList,
                key = { state -> state.id }) { state ->
                QuickTimerListItem(
                    modifier = Modifier,
                    state = state,
                    onRunningStateChange = { runningState ->
                        onRunningStateChange(
                            state.id,
                            runningState
                        )
                    },
                    onStateChange = { newState -> onStateChange(state.id, newState) }
                )
                Spacer(modifier = Modifier.height(Dimen.paddingSmall))
            }
        }
    }
}

@Composable
private fun QuickTimerListItem(
    state: TimerState,
    modifier: Modifier = Modifier,
    onStateChange: (Boolean) -> Unit = {},
    onRunningStateChange: (TimerStateType) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(Dimen.paddingLarge)
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
                text = "15 minutes",
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(Dimen.paddingMedium))
        TimerPB(
            progress = state.progress
        )

        Spacer(modifier = Modifier.height(Dimen.paddingMedium))
        Row() {
            if(state.state == TimerStateType.STARTED){
                IconButton(
                    onClick = { onRunningStateChange(TimerStateType.STARTED) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "",
                        tint = NeonBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }else {
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
            Spacer(modifier = Modifier.width(Dimen.paddingMedium))
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

@Preview
@Composable
private fun PreviewComponent() {
    val quickState = TimerState(
        id = 1,
        title = "quick timer 1",
        state = TimerStateType.STARTED,
        current = 10,
        max = 100
    )
    AppTheme {
        QuickTimerComponent(
            stateList = listOf(
                quickState,
                quickState.copy(id = 2, current = 25),
                quickState.copy(id = 3, current = 78)
            )
        )
    }
}