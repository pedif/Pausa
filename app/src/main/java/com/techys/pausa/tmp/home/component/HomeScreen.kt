package com.techys.pausa.tmp.home.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.techys.pausa.tmp.home.HomeViewModel
import com.techys.pausa.tmp.home.model.HomeState
import com.techys.pausa.tmp.model.PausaState
import com.techys.pausa.tmp.model.TimerState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.receiver.PausaServiceReceiver
import com.techys.pausa.tmp.service.PausaService
import com.techys.pausa.tmp.util.EyeTimerHelper
import com.techys.pausa.tmp.util.FocusTimerHelper
import com.techys.pausa.tmp.util.TimerConstants
import com.techys.pausa.ui.theme.AppTheme
import com.techys.pausa.ui.theme.Dimen

//SO we should separate logic from view, however, e.g. in sending a broadcast we should use a context and a context should not be passed to a viewmodel, so how
//to keep this separation fo concerns
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onStartFocusClick: () -> Unit = {},
    onStartQuickClick: () -> Unit = {}
) {
    val timerState by PausaService.state.collectAsState()
    val homeState by viewModel.state.collectAsState()
    val context = LocalContext.current
    HomeScreen(
        timerState = timerState,
        homeState = homeState,
        modifier = modifier,
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