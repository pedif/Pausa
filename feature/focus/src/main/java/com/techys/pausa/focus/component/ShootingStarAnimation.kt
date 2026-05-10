package com.techys.pausa.focus.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun ShootingStarsBackground(
    modifier: Modifier = Modifier,
    maxConcurrentStars: Int = 5,
    avgSpawnIntervalMs: Long = 1200L
) {
    var activeStars by remember { mutableStateOf(listOf<ShootingStarState>()) }
    var nextStarId by remember { mutableIntStateOf(0) }

    // Continuously spawn new stars at random intervals
    LaunchedEffect(Unit) {
        while (true) {
            if (activeStars.size < maxConcurrentStars) {
                val newStar = ShootingStarState(
                    id = nextStarId++,
                    startX = Random.nextFloat() * 0.7f + 0.15f,
                    startY = Random.nextFloat() * 0.2f,
                    length = 0.06f + Random.nextFloat() * 0.12f,
                    angle = 25f + Random.nextFloat() * 30f,
                    durationMs = 1800 + Random.nextInt(1200),
                    colorType = StarColorType.entries.random()
                )
                activeStars = activeStars + newStar
            }
            delay(Random.nextLong(avgSpawnIntervalMs * 2))
        }
    }

    Box(modifier = modifier) {
        activeStars.forEach { star ->
            ShootingStarParticle(
                star = star,
                onComplete = { completedId ->
                    activeStars = activeStars.filter { it.id != completedId }
                }
            )
        }
    }
}

private enum class StarColorType(
    val coreColor: Color,
    val glowColor: Color,
    val tailColor: Color
) {
    // Pure white (most common)
    WHITE(
        coreColor = Color.White,
        glowColor = Color.White,
        tailColor = Color.White
    ),
    // Blue-white (classic shooting star)
    BLUE_WHITE(
        coreColor = Color.White,
        glowColor = Color(0xFFB8D4FF),
        tailColor = Color(0xFF8BB8FF)
    ),
    // Warm yellow (rare, like a meteor)
    WARM_YELLOW(
        coreColor = Color(0xFFFFFBE6),
        glowColor = Color(0xFFFFE4A0),
        tailColor = Color(0xFFFFD070)
    ),
    // Soft pink (very rare, special)
    SOFT_PINK(
        coreColor = Color(0xFFFFE6F0),
        glowColor = Color(0xFFFFB8D9),
        tailColor = Color(0xFFFF8FC7)
    )
}

private data class ShootingStarState(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val length: Float,
    val angle: Float,
    val durationMs: Int,
    val colorType: StarColorType
)

@Composable
private fun ShootingStarParticle(
    star: ShootingStarState,
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
        val endX = startX + (star.length * screenW * kotlin.math.cos(angleRad)).toFloat()
        val endY = startY + (star.length * screenH * kotlin.math.sin(angleRad)).toFloat()

        val currentX = startX + (endX - startX) * progress
        val currentY = startY + (endY - startY) * progress

        // Alpha: fade in quickly, fade out slowly
        val alpha = when {
            progress < 0.15f -> progress / 0.15f
            progress > 0.6f -> (1f - progress) / 0.4f
            else -> 1f
        }

        // ─────────────────────────────────────────────
        // DRAW STAR TRAIL (light streak effect)
        // ─────────────────────────────────────────────
        val trailSegments = 20 // Performance: capped at 20 segments
        val trailLength = 0.4f // Trail extends 40% of total path

        if (trailSegments > 1) {
            val path = Path()
            var pathStarted = false

            for (i in trailSegments downTo 0) {
                val segmentProgress = (progress - (i.toFloat() / trailSegments) * trailLength)
                    .coerceIn(0f, progress)

                if (segmentProgress <= 0f) continue

                val segX = startX + (endX - startX) * segmentProgress
                val segY = startY + (endY - startY) * segmentProgress

                if (!pathStarted) {
                    path.moveTo(segX, segY)
                    pathStarted = true
                } else {
                    path.lineTo(segX, segY)
                }
            }

            // Draw trail with gradient-like effect using segments
            for (i in 0 until trailSegments) {
                val t1 = i.toFloat() / trailSegments
                val t2 = (i + 1).toFloat() / trailSegments

                val p1Progress = (progress - t1 * trailLength).coerceIn(0f, progress)
                val p2Progress = (progress - t2 * trailLength).coerceIn(0f, progress)

                if (p1Progress <= 0f || p2Progress <= 0f) continue

                val x1 = startX + (endX - startX) * p1Progress
                val y1 = startY + (endY - startY) * p1Progress
                val x2 = startX + (endX - startX) * p2Progress
                val y2 = startY + (endY - startY) * p2Progress

                // Segment alpha: older segments are more faded
                val segmentAlpha = ((1f - t1) * 0.6f + 0.1f) * alpha

                drawLine(
                    color = star.colorType.tailColor.copy(alpha = segmentAlpha),
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 1f + (1f - t1) * 1.5f, // Thicker near head
                    cap = StrokeCap.Round
                )
            }
        }

        // ─────────────────────────────────────────────
        // DRAW GLOWING COMET HEAD
        // ─────────────────────────────────────────────
        val glowColor = star.colorType.glowColor
        val coreColor = star.colorType.coreColor

        // Outer glow (largest, most transparent)
        drawCircle(
            color = glowColor.copy(alpha = alpha * 0.12f),
            radius = 20f,
            center = Offset(currentX, currentY)
        )

        // Middle glow
        drawCircle(
            color = glowColor.copy(alpha = alpha * 0.25f),
            radius = 12f,
            center = Offset(currentX, currentY)
        )

        // Inner glow
        drawCircle(
            color = coreColor.copy(alpha = alpha * 0.5f),
            radius = 6f,
            center = Offset(currentX, currentY)
        )

        // Bright core
        drawCircle(
            color = coreColor.copy(alpha = alpha),
            radius = 2.5f,
            center = Offset(currentX, currentY)
        )
    }
}

@Composable
fun NightSkyBackground(
    modifier: Modifier = Modifier,
    staticStarCount: Int = 80,
    maxShootingStars: Int = 5
) {
    Box(
        modifier = modifier
            .background(Color(0xFF050510)) // Near-black with slight blue
    ) {
        // Layer 1: Static twinkling stars
        StaticTwinklingStars(count = staticStarCount)

        // Layer 2: Shooting stars with trails & glow
        ShootingStarsBackground(
            maxConcurrentStars = maxShootingStars,
            avgSpawnIntervalMs = 1000L
        )
    }
}

@Composable
private fun StaticTwinklingStars(count: Int) {
    val stars = remember {
        List(count) {
            TwinklingStar(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 1.5f + 0.5f,
                twinkleSpeed = Random.nextFloat() * 2000 + 1000,
                baseAlpha = Random.nextFloat() * 0.4f + 0.3f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val time = System.nanoTime() / 1_000_000f
        stars.forEach { star ->
            val pulse = (kotlin.math.sin(time / star.twinkleSpeed * 2 * Math.PI) + 1f) / 2f
            val alpha = star.baseAlpha + pulse * 0.5f
            drawCircle(
                color = Color.White.copy(alpha = alpha.toFloat()),
                radius = star.size,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}

private data class TwinklingStar(
    val x: Float,
    val y: Float,
    val size: Float,
    val twinkleSpeed: Float,
    val baseAlpha: Float
)