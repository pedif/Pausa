package com.techys.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.core.model.PausaState
import com.techys.core.model.TimerState
import com.techys.core.model.TimerStateType
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.service.PausaService
import com.techys.core.util.TimerConstants
import com.techys.designsystem.theme.AppTheme
import com.techys.home.HomeViewModel
import com.techys.home.model.HomeState

//SO we should separate logic from view, however, e.g. in sending a broadcast we should use a context and a context should not be passed to a viewmodel, so how
//to keep this separation fo concerns
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onStartFocusClick: () -> Unit = {},
    onStartQuickClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val timerState by PausaService.state.collectAsState()
    val homeState by viewModel.state.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = { HomeTopBar(onSettingsClick = onSettingsClick) }
    ) { innerPadding ->
        HomeScreen(
            timerState = timerState,
            homeState = homeState,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            onEyeTimerRunningStateChange = { state ->
                PausaServiceReceiver.sendTimerStateUpdateBroadcast(
                    context,
                    id = TimerConstants.EYE_TIMER_ID,
                    state = state
                )
            },
            onFocusTimerRunningStateChange = { state ->
                if (state == TimerStateType.STARTED)
                    onStartFocusClick()
                else
                    PausaServiceReceiver.sendTimerStateUpdateBroadcast(
                        context,
                        id = TimerConstants.FOCUS_TIMER_ID,
                        state = state
                    )
            },
            onQuickTimerRunningStateChange = { id, state ->
                if (state == TimerStateType.STARTED)
                    onStartQuickClick()
                else
                    PausaServiceReceiver.sendTimerStateUpdateBroadcast(
                        context,
                        id = id,
                        state = state
                    )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    timerState: PausaState,
    homeState: HomeState,
    modifier: Modifier = Modifier,
    onEyeTimerStateChange: (Boolean) -> Unit = {},
    onEyeTimerRunningStateChange: (TimerStateType) -> Unit = {},
    onFocusTimerStateChange: (Boolean) -> Unit = {},
    onFocusTimerRunningStateChange: (TimerStateType) -> Unit = {},
    onQuickTimerStateChange: (Int, Boolean) -> Unit = { _, _ -> },
    onQuickTimerRunningStateChange: (Int, TimerStateType) -> Unit = { _, _ -> },
) {

    Box(modifier = modifier) {
        ParticleAnimation(modifier = Modifier.fillMaxSize())
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End
//        ) {
//            IconButton(
//                onClick = {},
//                modifier = Modifier.size(Dimen.buttonMedium)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Settings,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(Dimen.iconMedium)
//                )
//            }
//        }
        Column(modifier = Modifier.fillMaxSize()) {
            EyeTimerComponent(
                state = timerState.eyeTimer,
                modifier = Modifier,
                onStateChange = onEyeTimerStateChange,
                onRunningStateChange = onEyeTimerRunningStateChange
            )
            FocusTimerComponent(
                state = timerState.focusTimer,
                modifier = Modifier,
                onStateChange = onFocusTimerStateChange,
                onRunningStateChange = onFocusTimerRunningStateChange
            )
            QuickTimerComponent(
                stateList = timerState.quickTimers,
                modifier = Modifier,
                onStateChange = onQuickTimerStateChange,
                onRunningStateChange = onQuickTimerRunningStateChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    AppTheme {
        val state = TimerState(id = 0, state = TimerStateType.STARTED, current = 10, max = 40)
        val homeState = HomeState()
        HomeScreen(
            timerState = PausaState(
                eyeTimer = state,
                focusTimer = state
            ),
            homeState = homeState
        )
    }
}