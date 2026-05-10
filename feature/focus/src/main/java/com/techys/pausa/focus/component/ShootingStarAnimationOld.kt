package com.techys.pausa.focus.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
@Composable
fun ShootingStarsBackgrounds(
    modifier: Modifier = Modifier,
    maxConcurrentStars: Int = 5,
    avgSpawnIntervalMs: Long = 1200L
) {
    var activeStars by remember { mutableStateOf(listOf<ShootingStarStates>()) }
    var nextStarId by remember { mutableIntStateOf(0) }

    // Continuously spawn new stars at random intervals
    LaunchedEffect(Unit) {
        while (true) {
            if (activeStars.size < maxConcurrentStars) {
                val newStar = ShootingStarStates(
                    id = nextStarId++,
                    startX = Random.nextFloat() * 0.7f + 0.15f, // Avoid edges
                    startY = Random.nextFloat() * 0.2f,         // Top 20% of screen
                    length = 0.06f + Random.nextFloat() * 0.12f,
                    angle = 25f + Random.nextFloat() * 30f,
                    durationMs = 1800 + Random.nextInt(1200)
                )
                activeStars = activeStars + newStar
            }
            // Random delay between spawns (sporadic feel)
            delay(Random.nextLong(avgSpawnIntervalMs * 2))
        }
    }

    Box(modifier = modifier) {
        activeStars.forEach { star ->
            ShootingStarParticles(
                star = star,
                onComplete = { completedId ->
                    activeStars = activeStars.filter { it.id != completedId }
                }
            )
        }
    }
}

private data class ShootingStarStates(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val length: Float,
    val angle: Float,
    val durationMs: Int
)

@Composable
private fun ShootingStarParticles(
    star: ShootingStarStates,
    onComplete: (Int) -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(star.id) {
        val startTime = System.nanoTime()
        while (progress < 1f) {
            progress = ((System.nanoTime() - startTime) / 1_000_000f) / star.durationMs
            progress = progress.coerceIn(0f, 1f)
            delay(16L) // ~60fps
        }
        progress = 1f
        onComplete(star.id)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (progress <= 0f || progress >= 1f) return@Canvas

        val screenW = size.width
        val screenH = size.height

        // Calculate positions
        val startX = star.startX * screenW
        val startY = star.startY * screenH

        val angleRad = Math.toRadians(star.angle.toDouble())
        val endX = startX + (star.length * screenW * cos(angleRad)).toFloat()
        val endY = startY + (star.length * screenH * sin(angleRad)).toFloat()

        val currentX = startX + (endX - startX) * progress
        val currentY = startY + (endY - startY) * progress

        // Tail length (shrinks as star fades)
        val tailLength = (1f - progress) * star.length * screenW * 0.6f
        val tailStartX = currentX - tailLength * cos(angleRad).toFloat()
        val tailStartY = currentY - tailLength * sin(angleRad).toFloat()

        // Alpha: fade in quickly, fade out slowly
        val alpha = when {
            progress < 0.15f -> progress / 0.15f
            progress > 0.6f -> (1f - progress) / 0.4f
            else -> 1f
        }

        // ─────────────────────────────────────────────
        // DRAW TAIL (gradient from transparent to white)
        // ─────────────────────────────────────────────
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.White.copy(alpha = alpha * 0.3f),
                    Color.White.copy(alpha = alpha * 0.8f)
                ),
                start = Offset(tailStartX, tailStartY),
                end = Offset(currentX, currentY)
            ),
            start = Offset(tailStartX, tailStartY),
            end = Offset(currentX, currentY),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )

        // ─────────────────────────────────────────────
        // DRAW GLOWING COMET HEAD
        // ─────────────────────────────────────────────
        val glowColor = Color(0xFFB8D4FF) // Soft blue-white glow

        // Outer glow (largest, most transparent)
        drawCircle(
            color = glowColor.copy(alpha = alpha * 0.15f),
            radius = 18f,
            center = Offset(currentX, currentY)
        )

        // Middle glow
        drawCircle(
            color = glowColor.copy(alpha = alpha * 0.3f),
            radius = 10f,
            center = Offset(currentX, currentY)
        )

        // Inner glow
        drawCircle(
            color = Color.White.copy(alpha = alpha * 0.6f),
            radius = 5f,
            center = Offset(currentX, currentY)
        )

        // Bright core (solid white center)
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = 2f,
            center = Offset(currentX, currentY)
        )
    }
}


@Composable
private fun StaticTwinklingStarss(count: Int) {
    val stars = remember {
        List(count) {
            TwinklingStars(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 1.5f + 0.5f,
                twinkleSpeed = Random.nextFloat() * 2000 + 1000
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { star ->
            val pulse = (sin((System.nanoTime() / 1_000_000f) / star.twinkleSpeed * Math.PI) + 1f) / 2f
            val alpha = 0.3f + pulse * 0.7f
            drawCircle(
                color = Color.White.copy(alpha = alpha.toFloat()),
                radius = star.size,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}

private data class TwinklingStars(
    val x: Float,
    val y: Float,
    val size: Float,
    val twinkleSpeed: Float
)