package com.techys.pausa.tmp.home.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import com.techys.pausa.ui.theme.CardBackground
import com.techys.pausa.ui.theme.Dimen
import com.techys.pausa.ui.theme.NeonBlue
import com.techys.pausa.ui.theme.TextPrimary

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
            modifier = Modifier
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
                    text =  state.title ,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(text = "1 min and 30 sec",
                    color = TextPrimary)
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