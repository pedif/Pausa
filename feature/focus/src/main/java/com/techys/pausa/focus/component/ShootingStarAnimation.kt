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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.random.Random


/**
 * The base value for the animation duration
 */
private const val ANIM_DURATION_BASE = 1_800L

/**
 * The animation duration can be the base value plus a random number generated up to this value
 */
private const val ANIM_DURATION_MAX = 3_000L

/**
 * The shooting star animation can begin at different times between stars so we have a more
 * sporadic and random feeling of the stars
 */
private const val ANIM_DELAY_MAX = 1_000L

/**
 * The base value for the stars to be re-generated again
 */
private const val ANIM_SPAWN_RATE_BASE = 1_000L

/**
 * The max delay value for the next set of stars to be generated
 */
private const val ANIM_SPAWN_RATE_MAX = 5_000L

/**
 * The initial delay value for start of the animation
 */
private const val ANIM_START_DELAY = 2_500L
@Composable
fun ShootingStarsBackground(
    modifier: Modifier = Modifier,
    maxConcurrentStars: Int = 5,
    animDurationBase:Long = ANIM_DURATION_BASE,
    animDurationMax: Long = ANIM_DURATION_MAX,
    animSpawnRateBase:Long = ANIM_SPAWN_RATE_BASE,
    animSpawnRateMax: Long = ANIM_SPAWN_RATE_MAX,
    animStartDelay: Long = ANIM_START_DELAY
) {
    var activeStars by remember { mutableStateOf(listOf<ShootingStarState>()) }
    var nextStarId by remember { mutableIntStateOf(0) }

    // Continuously spawn new stars at random intervals
    LaunchedEffect(Unit) {
        delay(animStartDelay)
        while (true) {
            val iterationStartCount = Random.nextInt(1, maxConcurrentStars + 1)
            var longestDuration = 0L
            val starList = mutableListOf<ShootingStarState>()
            while (starList.size < iterationStartCount) {
                val animDuration =  Random.nextLong(animDurationBase, animDurationMax)
                val startDelay = Random.nextLong(0, ANIM_DELAY_MAX)
                val newStar = ShootingStarState(
                    id = nextStarId++,
                    startX = Random.nextFloat().coerceIn(0.1f, 0.9f) ,
                    startY = Random.nextFloat().coerceIn(0.1f, 0.9f) ,
                    length = 0.06f + Random.nextFloat() * 0.12f,
                    angle = (Random.nextFloat() * 2 - 1) * 360f,
                    durationMs = animDuration,
                    startDelay = startDelay,
                    colorType = StarColorType.entries.random()
                )
                longestDuration = max(longestDuration, startDelay + animDuration)
                starList.add(newStar)
            }
            activeStars = (starList)
            delay(longestDuration + Random.nextLong(animSpawnRateBase, animSpawnRateMax))
        }
    }

    Box(modifier = modifier) {
        activeStars.forEach { star ->
            ShootingStarParticle(star = star)
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
    val durationMs: Long,
    val startDelay: Long,
    val colorType: StarColorType
)

@Composable
private fun ShootingStarParticle(
    star: ShootingStarState,
    onComplete: (Int) -> Unit = {}
) {
    var progress by remember(star.id) { mutableFloatStateOf(0f) }

    LaunchedEffect(star.id) {
        delay(star.startDelay)
        val startTime = System.nanoTime()
        while (progress < 1f) {
            progress = ((System.nanoTime() - startTime) / 1_000_000f) / star.durationMs
            progress = progress.coerceIn(0f, 1f)
            delay(16L) // ~60fps
        }
        progress = 1f
        delay(300)
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
            maxConcurrentStars = maxShootingStars
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