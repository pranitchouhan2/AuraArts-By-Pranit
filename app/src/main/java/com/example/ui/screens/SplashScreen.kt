package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    useDarkTheme: Boolean = false
) {
    var animateLogo by remember { mutableStateOf(false) }
    var animateText by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_and_rotate")

    // Gentle logo scaling animation
    val scaleFactor by animateDpAsState(
        targetValue = if (animateLogo) 180.dp else 120.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    // Soft rotating particle angles
    val particleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_rotate"
    )

    LaunchedEffect(Unit) {
        delay(300)
        animateLogo = true
        delay(600)
        animateText = true
        delay(2200) // Beautiful delay to show animation
        onSplashComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Procedural Watercolor Background
        WatercolorBackground(useDarkTheme = useDarkTheme)

        // Floating particle brushstrokes
        Box(
            modifier = Modifier
                .size(310.dp)
                .rotate(particleAngle)
        ) {
            // Little floating pastel dots mimicking paint splatter/particles
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .background(AuraPink)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(AuraCyan.copy(alpha = 0.8f))
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(AuraPurple)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Animated Aura Arts paint logo (from assets if available, or generated image)
            Box(
                modifier = Modifier
                    .size(scaleFactor)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AuraLavender, AuraPink.copy(alpha = 0.3f))
                        )
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                // Main image generated previously
                Image(
                    painter = painterResource(id = R.drawable.aura_logo_icon),
                    contentDescription = "Aura Arts Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Typography Fade-in
            AnimatedVisibility(
                visible = animateText,
                enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(800)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aura Arts",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "CREATE • EXPRESS • INSPIRE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f),
                            letterSpacing = 3.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Modern loading transition spinner
            CircularProgressIndicator(
                color = AuraPurple.copy(alpha = 0.7f),
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
