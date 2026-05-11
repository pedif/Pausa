package com.techys.pausa.quick.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import com.techys.core.model.TimerState
import com.techys.core.service.PausaService
import com.techys.core.util.TimeUtil
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import kotlinx.coroutines.delay

@Composable
fun QuickEndScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {
    val state by PausaService.state.collectAsState()
    var remainingTime by remember { mutableIntStateOf(10) }

    QuickEndScreen(
        state = state.quickTimers.first(),
        seconds = remainingTime,
        modifier = modifier
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
fun QuickEndScreen(
    state: TimerState,
    seconds: Int,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(Dimen.paddingScreenHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${state.title} has just finished",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary)
        Text("Automatically dismissing this screen in" +
                " ${TimeUtil.getElapsedTimeLabel(seconds)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary)
    }

}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        QuickEndScreen(
            state = TimerState(id = 1),
            seconds = 10
        )
    }
}