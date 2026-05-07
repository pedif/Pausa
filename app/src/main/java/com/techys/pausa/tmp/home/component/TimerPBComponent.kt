package com.techys.pausa.tmp.home.component

import android.R
import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.pausa.ui.theme.AppTheme
import com.techys.pausa.ui.theme.Dimen
import com.techys.pausa.ui.theme.NeonBlue
import com.techys.pausa.ui.theme.ProgressTrack

//Shouldnt we just setup the matrial theme in a way that makes us not to directly set colors to texts and pbs and cards etc.????
@Composable
fun TimerPB(
    progress: Float,
    modifier: Modifier = Modifier
) {

    val p by animateFloatAsState(targetValue = progress,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label =  "pb animation"
    )
    LinearProgressIndicator(
        progress = { p },
        modifier = modifier
            .fillMaxWidth()
            .height(Dimen.progressbarHeight),
        color = NeonBlue,
        trackColor = NeonBlue.copy(alpha = 0.1f),
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Composable
fun GlowingPB(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceAtLeast(0.01f) + 0.06f)
                .fillMaxHeight(
                )
                .background(
                    color.copy(alpha = 0.6f),
                    RoundedCornerShape(24.dp)
                )
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }
}

@Composable
fun GPB( progress: Float, modifier: Modifier = Modifier,
         color: Color = MaterialTheme.colorScheme.primary) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier.fillMaxSize()
        ){
            //Draw tthe glow using a heavy blur
            //we draw a slightly larger circle to act as the light source
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.7f), Color.Transparent),
                    center = center,
                    radius = size.minDimension
                ),
                radius = size.minDimension
            )
        }
        //Apply the heavy gpu blur to the canvas above
        //we wrap it in a box to appl the effect to the specific layer

        Box(
            modifier = Modifier.fillMaxSize().graphicsLayer{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    renderEffect = BlurEffect(40f,40f, TileMode.Clamp)
                }
            }
        ){
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = color.copy(alpha = 0.5f),
                    radius = size.minDimension /2f
                )
            }
        }
        //The hard core the actual bar//.this stays asharp and  is not blurred
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            gapSize = 0.dp,
            strokeWidth = 12.dp
        )
    }
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

@Preview
@Composable
private fun PreviewGlowingComponent() {
    AppTheme {
        GlowingPB(progress = 0.8f,
            modifier = Modifier.height(50.dp))
    }
}

@Preview
@Composable
private fun PreviewGBPComponent() {
    AppTheme {
        GPB(progress = 0.8f,
            modifier = Modifier.height(350.dp))
    }
}