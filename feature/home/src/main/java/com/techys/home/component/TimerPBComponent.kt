package com.techys.home.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue

//Shouldnt we just setup the matrial theme in a way that makes us not to directly set colors to texts and pbs and cards etc.????
@Composable
fun TimerPB(
    progress: Float,
    modifier: Modifier = Modifier
) {

    val animatable = remember { Animatable(100f) }

    LaunchedEffect(progress) {
        animatable.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 950, easing = LinearEasing)
        )
    }

    LinearProgressIndicator(
        progress = { animatable.value },
        modifier = modifier
            .fillMaxWidth()
            .height(Dimen.progressbarHeight),
        color = NeonBlue,
        trackColor = NeonBlue.copy(alpha = 0.1f),
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        TimerPB(
            progress = 0.8f,
            modifier = Modifier.height(50.dp)
        )
    }
}
