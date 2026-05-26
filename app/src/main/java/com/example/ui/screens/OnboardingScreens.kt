package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val illustrationType: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreens(
    onOnboardingComplete: () -> Unit,
    useDarkTheme: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    val pages = remember {
        listOf(
            OnboardingPageData(
                title = "Handmade Masterpieces",
                subtitle = "Ethereal Expressions",
                description = "Immerse yourself in authentic physical expressions. From deep, cosmic Shiva artwork to tranquil radial mandalas and lifelike portrait sketches.",
                icon = Icons.Default.Brush,
                iconColor = AuraPurple,
                illustrationType = "shiva"
            ),
            OnboardingPageData(
                title = "Bespoke Commissions",
                subtitle = "Tailored to Your Soul",
                description = "Collaborate directly with Pranit Chouhan. Share your references, outline your spiritual visions, select custom dimensions, and build the perfect space decor.",
                icon = Icons.Default.Draw,
                iconColor = AuraCyan,
                illustrationType = "commission"
            ),
            OnboardingPageData(
                title = "Artistic Sanctuary",
                subtitle = "Vibrant Community Feed",
                description = "Savor microscopic progression reels, double-tap to like, support local handcrafted art, and order pieces seamlessly via direct message support.",
                icon = Icons.Default.AutoAwesome,
                iconColor = AuraPink,
                illustrationType = "community"
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f),
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onOnboardingComplete() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Central Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Aesthetic Illustrative Canvas Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(1.1f)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        AuraLavender.copy(alpha = if (useDarkTheme) 0.1f else 0.4f),
                                        AuraPink.copy(alpha = if (useDarkTheme) 0.1f else 0.3f)
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OnboardingIllustration(
                            type = page.illustrationType,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = page.subtitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = page.iconColor,
                            letterSpacing = 2.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.75f),
                            lineHeight = 24.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Bottom Navigation and Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page Dots Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until pages.size) {
                        val active = pagerState.currentPage == i
                        val widthState = animateDpAsState(
                            targetValue = if (active) 24.dp else 8.dp,
                            label = "onboard_dot_width"
                        )
                        Box(
                            modifier = Modifier
                                .size(height = 8.dp, width = widthState.value)
                                .clip(CircleShape)
                                .background(
                                    if (active) AuraPurple else (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(
                                        alpha = 0.3f
                                    )
                                )
                        )
                    }
                }

                // Primary Next / Continue Button
                val isLastPage = pagerState.currentPage == pages.size - 1
                Button(
                    onClick = {
                        if (isLastPage) {
                            onOnboardingComplete()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuraPurple,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isLastPage) "Explore Aura" else "Next",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Icon",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingIllustration(
    type: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (type) {
            "shiva" -> {
                // Shiva aura radiating soft waves
                for (i in 1..4) {
                    drawCircle(
                        color = AuraPurple.copy(alpha = 0.15f * (5 - i)),
                        radius = w * (0.12f * i),
                        center = Offset(w / 2, h / 2)
                    )
                }
                // Symmetrical brushstroke curves represent meditative energy
                drawCircle(
                    color = AuraCyan.copy(alpha = 0.5f),
                    radius = w * 0.15f,
                    center = Offset(w / 2, h / 2)
                )
                // crescent
                drawCircle(
                    color = Color.White,
                    radius = w * 0.06f,
                    center = Offset(w * 0.6f, h * 0.4f)
                )
                drawCircle(
                    color = AuraPurple,
                    radius = w * 0.06f,
                    center = Offset(w * 0.64f, h * 0.38f)
                )
            }
            "commission" -> {
                // Intersecting custom shapes as drafting a design
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(AuraCyan.copy(alpha = 0.4f), AuraPink.copy(alpha = 0.5f))
                    ),
                    topLeft = Offset(w * 0.2f, h * 0.25f),
                    size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.5f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
                )
                // Center heart/sparkle
                drawCircle(
                    color = AuraPurple.copy(alpha = 0.7f),
                    radius = w * 0.1f,
                    center = Offset(w / 2, h / 2)
                )
            }
            "community" -> {
                // Star patterns or spirals to describe dynamic reels/community feeds
                for (i in 0..10) {
                    val angle = Math.toRadians(i * 36.0)
                    val r = w * 0.28f
                    val x = (Math.cos(angle) * r).toFloat() + w/2
                    val y = (Math.sin(angle) * r).toFloat() + h/2
                    drawCircle(
                        color = AuraPink.copy(alpha = 0.61f),
                        radius = 12f,
                        center = Offset(x, y)
                    )
                }
                // Central core glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AuraCyan, Color.Transparent),
                        center = Offset(w/2, h/2),
                        radius = w * 0.25f
                    ),
                    radius = w * 0.25f,
                    center = Offset(w/2, h/2)
                )
            }
        }
    }
}
