package com.techys.home.component

import android.text.format.DateUtils
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.core.util.TimeUtil
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import com.techys.designsystem.theme.TextPrimary
import com.techys.pausa.core.R

@Composable
fun FocusTimerComponent(
    state: TimerState,
    modifier: Modifier = Modifier,
    onStateChange: (Boolean) -> Unit = {},
    onRunningStateChange: (TimerStateType) -> Unit = {}
) {

    TimerCardComponent(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(Dimen.large)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier= Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.radix_ic_timer),
                    contentDescription = "",
                    tint = NeonBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(Dimen.small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text =  state.title ,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(text = TimeUtil.getElapsedTimeLabel(state.remainingTime),
                    color = TextPrimary)
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
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        FocusTimerComponent(
            state = TimerState(
                id = 0,
                state = TimerStateType.STARTED,
                current = 45,
                max = 60,
                title = "Zen mode"
            )
        )
    }
}