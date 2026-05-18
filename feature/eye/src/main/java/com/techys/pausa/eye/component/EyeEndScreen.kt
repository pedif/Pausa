package com.techys.pausa.eye.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.core.util.TimeUtil
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.core.R
import com.techys.pausa.eye.EyeEndViewModel
import com.techys.pausa.eye.model.EyeEndState

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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${state.title} has just finished",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                stringResource(
                    R.string.timer_end_title,
                    TimeUtil.getElapsedTimeLabel(state.current)
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        EyeEndScreen(
            state = EyeEndState(title = "eye timer")
        )
    }
}