package com.techys.pausa.tmp.home.component

import android.R
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.random.Random

@Composable
fun ParticleAnimation(
    particleColor: Color = MaterialTheme.colorScheme.primary,
    particleCount: Int = 10,
    modifier: Modifier = Modifier
) {

    val particles = remember {
        List(particleCount) {
            Particle(
                start = Offset(Random.nextFloat(), Random.nextFloat()),
                control = Offset(Random.nextFloat(), Random.nextFloat()),
                end = Offset(Random.nextFloat(), Random.nextFloat()),
                speed = Random.nextFloat() * 0.002f + 0.001f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )



    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { star ->
          //Quadratic bezier formula B(t) = (1-t)^2*P0 + 2(1-t)t*P1 + t^2*P2
            val x= (1-star.progress).pow(2) * star.start.x + 2*(1-star.progress) * star.progress * star.control.x + star.progress.pow(2)*star.end.x
            val y= (1-star.progress).pow(2) * star.start.y + 2*(1-star.progress) * star.progress * star.control.y + star.progress.pow(2)*star.end.y
            val currentPos = Offset(x,y)

            //To draw the tail we calculate a point sllightly behind the current position
            //on the same curve to create a curved trail
            val prevProgress = (star.progress - 0.05f).coerceAtLeast(0f)
            val prevX= (1-prevProgress).pow(2) * star.start.x + 2*(1-prevProgress) * prevProgress * star.control.x + prevProgress.pow(2)*star.end.x
            val prevY= (1-prevProgress).pow(2) * star.start.y + 2*(1-prevProgress) * prevProgress * star.control.y + prevProgress.pow(2)*star.end.y
            //Draw the tail
            drawLine(
                color = Color.White.copy(alpha = star.alpha* 0.5f),
                start = Offset(prevX, prevY),
                end = currentPos,
                strokeWidth = 2f
            )

            //Draw the head
            drawCircle(
                color = Color.White.copy(alpha = star.alpha),
                radius = 4f,
                center= currentPos
            )
            star.progress += star.speed
            star.alpha -= 0.00f
        }
    }
}

data class Particle(
    val start: Offset,
    val control: Offset,
    val end: Offset,
    var progress: Float = 0f,
    var alpha: Float = 1f,
    val speed: Float
)