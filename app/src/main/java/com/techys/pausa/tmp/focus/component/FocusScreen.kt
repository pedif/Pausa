package com.techys.pausa.tmp.focus.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.techys.pausa.tmp.focus.FocusViewModel
import com.techys.pausa.tmp.focus.model.FocusState
import com.techys.pausa.tmp.home.component.TimerPicker
import com.techys.pausa.tmp.model.PausaState
import com.techys.pausa.tmp.model.TimerState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.service.PausaService
import com.techys.pausa.tmp.util.getElapsedTimeLabel
import com.techys.pausa.ui.theme.AppTheme

@Composable
fun FocusScreen(
    viewModel: FocusViewModel,
    modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsState()
    FocusScreen(
        state = state,
        modifier = modifier,
        onTimeChanged = viewModel::onTimeChanged
    )
}

@Composable
private fun FocusScreen(
    state: FocusState,
    modifier: Modifier = Modifier,
    onTimeChanged:(Int)->Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            progress = { state.progress },
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            modifier = Modifier
                .size(300.dp)
                .offset(y = -100.dp)
                .align(Alignment.Center),
            gapSize = 0.dp,
            strokeWidth = 25.dp,
            strokeCap = StrokeCap.Square
        )

        Text(
            text = state.timeLabel,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )


        Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            TimerPicker(
                modifier = Modifier,
                onTimeChanged = onTimeChanged
            )

        }
    }
}


@Preview(showBackground = true)
@Preview(backgroundColor = 0XFF1212FF)
@Composable
private fun PreviewScreen() {
    AppTheme {
        FocusScreen(
            state = FocusState()
        )
    }
}