package com.techys.pausa.focus.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerState
import com.techys.core.service.PausaService
import com.techys.core.util.TimeUtil
import com.techys.designsystem.component.PausaButton
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.core.R
import kotlinx.coroutines.delay

@Composable
fun FocusEndScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {
    val state by PausaService.state.collectAsState()
    var remainingTime by remember { mutableIntStateOf(30) }

    FocusEndScreen(
        state = state.focusTimer,
        seconds = remainingTime,
        modifier = modifier,
        onFinishClick = onFinished
    )

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1_000)
            remainingTime--
        }
        onFinished()
    }
}

@Composable
fun FocusEndScreen(
    state: TimerState,
    seconds: Int,
    modifier: Modifier = Modifier,
    onFinishClick: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimen.paddingScreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.timer_end_focus_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Dimen.small))
        Text(
            stringResource(R.string.timer_end_focus_desc, state.max / 60),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(
                R.string.timer_end_auto_dismiss,
                TimeUtil.getElapsedTimeLabel(seconds)
            ),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Dimen.small))
        PausaButton(
            text = stringResource(R.string.timer_end_action_dismiss_immediate),
            onClick = onFinishClick
        )
    }

}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        FocusEndScreen(
            state = TimerState(id = 1),
            seconds = 10
        )
    }
}