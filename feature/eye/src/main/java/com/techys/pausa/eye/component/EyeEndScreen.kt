package com.techys.pausa.eye.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.core.model.TimerState
import com.techys.core.service.PausaService
import com.techys.core.util.TimeUtil
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.eye.EyeEndViewModel
import com.techys.pausa.eye.model.EyeEndState
import kotlinx.coroutines.delay

@Composable
fun EyeEndScreen(
    modifier: Modifier = Modifier,
    viewModel: EyeEndViewModel = hiltViewModel(),
    onFinished: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    EyeEndScreen(
        state = state,
        modifier = modifier
    )

    LaunchedEffect(state) {
        if (state.current == 0)
            onFinished()
    }
}

@Composable
fun EyeEndScreen(
    state: EyeEndState,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimen.paddingScreen)
    ) {
        ShootingStarsBackground(modifier = Modifier.fillMaxSize(), maxConcurrentStars = 7)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${state.title} has just finished",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Automatically dismissing this screen in" +
                        " ${TimeUtil.getElapsedTimeLabel(state.current)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        EyeEndScreen(
            state = EyeEndState( title = "eye timer")
        )
    }
}