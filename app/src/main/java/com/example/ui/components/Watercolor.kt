package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

@Composable
fun WatercolorBackground(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean = false
) {
    // Elegant floating coordinates to animate custom watercolor elements
    val infiniteTransition = rememberInfiniteTransition(label = "watercolor_anim")

    val floatAnim1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radial_one"
    )

    val floatAnim2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radial_two"
    )

    val backgroundBrush = remember(useDarkTheme) {
        if (useDarkTheme) {
            Brush.linearGradient(
                colors = listOf(
                    GlassBgGradStartDark,
                    GlassBgGradViaDark,
                    GlassBgGradEndDark
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    GlassBgGradStartLight,
                    GlassBgGradViaLight,
                    GlassBgGradEndLight
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            if (width == 0f || height == 0f) return@Canvas

            // Radiant Purple watercolor blotch
            val rad1 = Math.toRadians(floatAnim1.toDouble())
            val c1X = (width * 0.3f) + (Math.cos(rad1) * 80).toFloat()
            val c1Y = (height * 0.25f) + (Math.sin(rad1) * 80).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AuraPurple.copy(alpha = if (useDarkTheme) 0.15f else 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(c1X, c1Y),
                    radius = width * 0.55f
                ),
                radius = width * 0.55f,
                center = Offset(c1X, c1Y),
                blendMode = if (useDarkTheme) BlendMode.Screen else BlendMode.Plus
            )

            // Radiant Cyan/Blue watercolor blotch
            val rad2 = Math.toRadians(floatAnim2.toDouble())
            val c2X = (width * 0.7f) + (Math.cos(rad2) * 110).toFloat()
            val c2Y = (height * 0.65f) + (Math.sin(rad2) * 110).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AuraCyan.copy(alpha = if (useDarkTheme) 0.15f else 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(c2X, c2Y),
                    radius = width * 0.6f
                ),
                radius = width * 0.6f,
                center = Offset(c2X, c2Y),
                blendMode = if (useDarkTheme) BlendMode.Screen else BlendMode.Plus
            )

            // Delicate Pastel Pink splatter/accent
            val c3X = (width * 0.4f) + (Math.sin(rad1) * 50).toFloat()
            val c3Y = (height * 0.8f) + (Math.cos(rad1) * 50).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AuraPink.copy(alpha = if (useDarkTheme) 0.12f else 0.22f),
                        Color.Transparent
                    ),
                    center = Offset(c3X, c3Y),
                    radius = width * 0.45f
                ),
                radius = width * 0.45f,
                center = Offset(c3X, c3Y),
                blendMode = if (useDarkTheme) BlendMode.Screen else BlendMode.Plus
            )
        }
    }
}

// Helper block to represent mock-ups of artworks since coil would use standard loaded URL resources
@Composable
fun BeautifulArtworkImagePlaceholder(
    artworkId: String,
    modifier: Modifier = Modifier
) {
    // Rich procedural art visuals mimicking the artwork types using canvas to keep app incredibly lightweight & beautiful
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Background splash
        val brush = when {
            artworkId.startsWith("shiva") -> {
                Brush.linearGradient(
                    colors = listOf(AuraPurple, SecondaryPurple, AuraCyan),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )
            }
            artworkId.startsWith("mandala") -> {
                Brush.radialGradient(
                    colors = listOf(AuraPurple, AuraPink, Color.Transparent),
                    center = Offset(w/2, h/2),
                    radius = w * 0.6f
                )
            }
            artworkId.startsWith("portrait") -> {
                Brush.linearGradient(
                    colors = listOf(AuraLavender, AuraPink, AuraPurple),
                    start = Offset(0f, h),
                    end = Offset(w, 0f)
                )
            }
            else -> {
                Brush.linearGradient(
                    colors = listOf(AuraCyan, TertiaryBlue, AuraPurple),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )
            }
        }

        drawRect(brush = brush)

        // Draw overlay aesthetic items
        if (artworkId.startsWith("shiva")) {
            // Draw crescent Moon representational curve
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = w * 0.08f,
                center = Offset(w * 0.7f, h * 0.25f)
            )
            drawCircle(
                color = AuraPurple.copy(alpha = 0.9f),
                radius = w * 0.08f,
                center = Offset(w * 0.74f, h * 0.23f)
            )

            // Draw trident/Trishul geometric line elements
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(w * 0.5f, h * 0.3f),
                end = Offset(w * 0.5f, h * 0.85f),
                strokeWidth = 5f
            )
            // Head curves
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(w * 0.4f, h * 0.4f),
                end = Offset(w * 0.6f, h * 0.4f),
                strokeWidth = 4f
            )
        } else if (artworkId.startsWith("mandala")) {
            // Symmetrical Concentric circle lines to mimic Mandala Art
            for (i in 1..6) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.23f),
                    radius = (w * 0.08f * i),
                    center = Offset(w / 2, h / 2),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
                )
                // radiating rays
                for (j in 0..11) {
                    val angle = Math.toRadians(j * 30.0)
                    val x = (Math.cos(angle) * (w * 0.08f * i)).toFloat() + w/2
                    val y = (Math.sin(angle) * (w * 0.08f * i)).toFloat() + h/2
                    drawCircle(
                        color = Color.White.copy(alpha = 0.15f),
                        radius = 4f,
                        center = Offset(x, y)
                    )
                }
            }
        } else if (artworkId.startsWith("portrait")) {
            // Subtle abstract artistic portrait outlines
            drawCircle(
                color = Color.White.copy(alpha = 0.28f),
                radius = w * 0.2f,
                center = Offset(w * 0.5f, h * 0.4f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )
            drawLine(
                color = Color.White.copy(alpha = 0.28f),
                start = Offset(w * 0.5f, h * 0.5f),
                end = Offset(w * 0.5f, h * 0.65f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.28f),
                start = Offset(w * 0.43f, h * 0.75f),
                end = Offset(w * 0.57f, h * 0.75f),
                strokeWidth = 3f
            )
        } else {
            // Abstract wave curves for crafts and custom panel decor
            for (i in 0..3) {
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, h * (0.3f + i * 0.15f)),
                    end = Offset(w, h * (0.2f + i * 0.15f)),
                    strokeWidth = 4f
                )
            }
        }
    }
}
