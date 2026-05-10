package com.techys.pausa.focus.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerStateType
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.TimerConstants
import com.techys.designsystem.theme.AppTheme
import com.techys.pausa.focus.FocusViewModel
import com.techys.pausa.focus.model.FocusState
import com.google.android.material.timepicker.MaterialTimePicker
import com.ozcanalasalvar.wheelview.WheelView

@Composable
fun FocusScreen(
    viewModel: FocusViewModel,
    modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    FocusScreen(
        state = state,
        modifier = modifier,
        onTimeChanged = viewModel::onTimeChanged,
        onStartNewTimer = {
            PausaServiceReceiver.sendTimerInfoUpdateBroadcast(
                context,
                TimerConstants.FOCUS_TIMER_ID,
                interval = state.time,
                shouldStartImmediately = true,
                title = null
            )
        },
        onTimerRunningStateChange = { runningState ->
            PausaServiceReceiver.sendTimerStateUpdateBroadcast(
                context,
                id = TimerConstants.FOCUS_TIMER_ID,
                state = runningState
            )
        }
    )
}

@Composable
private fun FocusScreen(
    state: FocusState,
    modifier: Modifier = Modifier,
    onTimeChanged: (Int) -> Unit = {},
    onStartNewTimer: () -> Unit = {},
    onTimerRunningStateChange: (TimerStateType) -> Unit = {}
) {
    val progress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "pb animation"
    )
    Box(modifier = modifier.fillMaxSize()) {
        NightSkyBackground(
            modifier = Modifier.fillMaxSize(),
            staticStarCount = 100,
            maxShootingStars = 5
        )
        CircularProgressIndicator(
            progress = { progress },
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            gapSize = 0.dp,
            strokeWidth = 25.dp,
            strokeCap = StrokeCap.Butt
        )

        Text(
            text = state.timeLabel,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )
//        Image(imageVector = Icons.Filled.P)

        val isNew = state.progress == 1f
        // First composable
        AnimatedVisibility(
            visible = isNew,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            StartFocusComponent(
                onTimerStartedClicked = onStartNewTimer,
                onTimeChanged = onTimeChanged
            )
        }

        // Second composable (mutually exclusive)
        AnimatedVisibility(
            visible = !isNew,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier
                    .align(Alignment.BottomCenter)
        ) {
            RunningFocusComponent(
                isTimerPaused = state.runningState == TimerStateType.PAUSED,
                onTimerStopClicked = { onTimerRunningStateChange(TimerStateType.STOPPED) },
                onTimerPauseClicked = { onTimerRunningStateChange(TimerStateType.PAUSED) },
                onTimerResumeClicked = { onTimerRunningStateChange(TimerStateType.STARTED) }
            )
        }
//        if (state.progress == 1f) {
//            StartFocusComponent(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter),
//                onTimerStartedClicked = onStartNewTimer,
//                onTimeChanged = onTimeChanged
//            )
//        } else {
//            RunningFocusComponent(
//                isTimerPaused = state.runningState == TimerStateType.PAUSED,
//                modifier = Modifier
//                    .align(Alignment.BottomCenter),
//                onTimerStopClicked = { onTimerRunningStateChange(TimerStateType.STOPPED) },
//                onTimerPauseClicked = { onTimerRunningStateChange(TimerStateType.PAUSED) },
//                onTimerResumeClicked = { onTimerRunningStateChange(TimerStateType.STARTED) }
//            )
//        }
    }
}


@Preview(showBackground = true)
@Preview(backgroundColor = 0XFF1212FF)
@Composable
private fun PreviewScreen() {
    AppTheme {
        FocusScreen(
            state = FocusState(progress = 0.99f)
        )
    }
}