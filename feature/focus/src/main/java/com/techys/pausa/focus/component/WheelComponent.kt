package com.techys.pausa.focus.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun <T> WheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = 48.dp,
    selectedItemColor: Color = Color.Cyan,
    unselectedItemColor: Color = Color.Gray,
    itemTextStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val visibleItemsHalf = visibleItemsCount / 2

    val scaledDensity = LocalDensity.current
    val itemHeightPx = with(scaledDensity) { itemHeight.toPx() }
    val totalHeight = itemHeightPx * visibleItemsCount

    val animatedOffset = remember { Animatable(0f) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var lastVelocity by remember { mutableFloatStateOf(0f) }

    // Calculate current index
    val currentIndex = remember(selectedIndex, animatedOffset.value) {
        val offset = animatedOffset.value + dragOffset
        val rawIndex = selectedIndex - (offset / itemHeightPx).roundToInt()
        rawIndex.coerceIn(0, items.lastIndex)
    }

    // Watch drag state changes - this IS a suspend context
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            // Snap animation
            animatedOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            // Calculate and notify final index
            val totalOffset = dragOffset + animatedOffset.value
            val indexOffset = (totalOffset / itemHeightPx).roundToInt()
            val targetIndex = (selectedIndex - indexOffset).coerceIn(0, items.lastIndex)
            onSelectedIndexChange(targetIndex)
            dragOffset = 0f
        }
    }
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            animatedOffset.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))

            val totalOffset = dragOffset + animatedOffset.value
            val indexOffset = (totalOffset / itemHeightPx).roundToInt()
            val targetIndex = (selectedIndex - indexOffset).coerceIn(0, items.lastIndex)
            onSelectedIndexChange(targetIndex)
            dragOffset = 0f
        }
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .pointerInput(Unit) {
                val decay = exponentialDecay<Float>( 1f)

                awaitEachGesture {
                    val first = awaitFirstDown(requireUnconsumed = false)
                    isDragging = true
                    dragOffset = 0f

                    var totalDrag = 0f

                    do {
                        val event = awaitPointerEvent()
                        val drag = event.changes.sumOf { it.positionChange().y.toDouble() }.toFloat()
                        totalDrag += drag
                        dragOffset = totalDrag
                        event.changes.forEach { it.consume() }
                    } while (event.changes.any { it.pressed })

                    // Calculate velocity from last drag
                    val velocity = first.previousPosition.y

                    // Apply fling if velocity is high
                    if (kotlin.math.abs(velocity) > 100) {
                        val targetOffset = decay.calculateTargetValue(totalDrag, velocity)
                        dragOffset = targetOffset
                    }

                    isDragging = false
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerY = totalHeight / 2

            for (i in -visibleItemsHalf..visibleItemsHalf) {
                val itemIndex = currentIndex + i
                if (itemIndex !in items.indices) continue

                val itemCenterY = centerY + (i * itemHeightPx) + (dragOffset % itemHeightPx)
                val distanceFromCenter = kotlin.math.abs(itemCenterY - centerY)
                val normalizedDistance = (distanceFromCenter / (itemHeightPx * visibleItemsHalf)).coerceIn(0f, 1f)

                val scale = 1f - (normalizedDistance * 0.3f)
                val alpha = 1f - (normalizedDistance * 0.7f)

                // Selection background
                if (normalizedDistance < 0.15f) {
                    drawRect(
                        color = selectedItemColor.copy(alpha = 0.1f),
                        topLeft = Offset(0f, itemCenterY - itemHeightPx / 2),
                        size = Size(size.width, itemHeightPx)
                    )
                }

                // Draw text
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = if (normalizedDistance < 0.15f) {
                            selectedItemColor.toArgb()
                        } else {
                            unselectedItemColor.copy(alpha = alpha).toArgb()
                        }
                        textSize = itemTextStyle.fontSize.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }

                    val text = items[itemIndex].toString()
                    val textY = itemCenterY + (paint.textSize / 3)

                    drawText(text, size.width / 2, textY, paint)
                }
            }

            // Selection indicators
            drawLine(
                color = selectedItemColor,
                start = Offset(0f, centerY - itemHeightPx / 2),
                end = Offset(size.width, centerY - itemHeightPx / 2),
                strokeWidth = 2f
            )
            drawLine(
                color = selectedItemColor,
                start = Offset(0f, centerY + itemHeightPx / 2),
                end = Offset(size.width, centerY + itemHeightPx / 2),
                strokeWidth = 2f
            )
        }
    }
}