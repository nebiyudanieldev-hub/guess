package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import java.util.Random

private data class ConfettiParticle(
    val startX: Float,
    val speedY: Float,
    val speedX: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiEffect(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    val colors = listOf(
        Color(0xFFFF5252),
        Color(0xFFFFD740),
        Color(0xFF69F0AE),
        Color(0xFF40C4FF),
        Color(0xFFE040FB),
        Color(0xFFFF6E40)
    )

    val particles = remember {
        val rand = Random(42)
        List(60) {
            ConfettiParticle(
                startX = rand.nextFloat(),
                speedY = 0.5f + rand.nextFloat() * 0.8f,
                speedX = (rand.nextFloat() - 0.5f) * 0.3f,
                color = colors[rand.nextInt(colors.size)],
                size = 14f + rand.nextFloat() * 14f,
                rotationSpeed = rand.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2800, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val p = progress.value

        particles.forEach { pt ->
            val curY = -50f + (h + 100f) * (p * pt.speedY)
            val curX = (pt.startX * w) + (pt.speedX * w * p)
            if (curY in -50f..(h + 50f)) {
                drawRect(
                    color = pt.color.copy(alpha = (1f - p * 0.7f).coerceIn(0f, 1f)),
                    topLeft = Offset(curX, curY),
                    size = Size(pt.size, pt.size * 0.6f)
                )
            }
        }
    }
}
