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
import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.core.util.TimeUtil
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.designsystem.theme.TextPrimary

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
                Spacer(modifier = Modifier.height(Dimen.small))
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
            .padding(Dimen.large)
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
                text = TimeUtil.getElapsedTimeLabel(state.remainingTime),
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(Dimen.medium))
        TimerPB(
            progress = state.progress
        )
        Spacer(modifier = Modifier.height(Dimen.medium))
        TimerControlComponent(
            state = state.state,
            modifier= Modifier,
            onRunningStateChange = onRunningStateChange
        )
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