package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArtReel
import com.example.ui.AppViewModel
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReelsScreen(
    viewModel: AppViewModel,
    useDarkTheme: Boolean = false
) {
    val reels by viewModel.reels.collectAsState()
    var activeIndex by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    // Engagement values (simulate bookmark likes comments)
    val activeReel = remember(activeIndex, reels) {
        if (reels.isNotEmpty()) reels[activeIndex] else null
    }

    // Rotating vinyl overlay animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    // Double-tap heart animation states
    var doubleTapHeartVisible by remember { mutableStateOf(false) }
    val doubleTapHeartScale = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (activeReel == null) {
            CircularProgressIndicator(color = AuraPurple)
        } else {
            // Simulated Video Frame Background with customized dynamic aura flow gradients
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(activeIndex) {
                        detectTapGestures(
                            onDoubleTap = {
                                viewModel.toggleLikeReel(activeReel.id, activeReel.isLiked)
                                coroutineScope.launch {
                                    doubleTapHeartVisible = true
                                    doubleTapHeartScale.snapTo(0f)
                                    doubleTapHeartScale.animateTo(
                                        targetValue = 1.6f,
                                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                    )
                                    doubleTapHeartScale.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(300)
                                    )
                                    doubleTapHeartVisible = false
                                }
                            }
                        )
                    }
            ) {
                // Procedural dynamic watercolor patterns mimicking video flows
                WatercolorBackground(useDarkTheme = true)

                // Simulated progress timeline overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                        .align(Alignment.BottomCenter)
                ) {
                    // Infinite slow loop simulating video timeline progress
                    val progressTransition = rememberInfiniteTransition(label = "prog")
                    val progressWidth by progressTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(9000, easing = LinearEasing)
                        ),
                        label = "percent"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressWidth)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(colors = listOf(AuraPurple, AuraPink))
                            )
                    )
                }

                // Rotating Paint Palette / Disc in bottom right
                Box(
                    modifier = Modifier
                        .padding(bottom = 76.dp, end = 20.dp)
                        .size(54.dp)
                        .align(Alignment.BottomEnd)
                        .rotate(rotationAngle)
                        .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(AuraPurple, AuraPink, AuraCyan, AuraPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(26.dp)) {
                        // Drawing microscopic paint drops on palette
                        drawCircle(color = Color.White, radius = 4f, center = center)
                        drawCircle(color = AuraPink, radius = 3.5f, center = androidx.compose.ui.geometry.Offset(size.width * 0.3f, size.height * 0.3f))
                        drawCircle(color = AuraCyan, radius = 3.5f, center = androidx.compose.ui.geometry.Offset(size.width * 0.7f, size.height * 0.3f))
                    }
                }

                // Large Video play overlay floating center to denote arttimelapse nature
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Simulated creation video play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Rising Heart element overlay on Double Tap
                if (doubleTapHeartVisible) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = AuraPink,
                        modifier = Modifier
                            .size(90.dp)
                            .scale(doubleTapHeartScale.value)
                            .align(Alignment.Center)
                    )
                }

                // engagement counters sidebar (aligned right)
                Column(
                    modifier = Modifier
                        .padding(bottom = 150.dp, end = 20.dp)
                        .align(Alignment.BottomEnd),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Likes Action
                    IconButton(
                        onClick = { viewModel.toggleLikeReel(activeReel.id, activeReel.isLiked) },
                        modifier = Modifier
                            .size(46.dp)
                            .frostedGlass(CircleShape, useDarkTheme = true)
                    ) {
                        Icon(
                            imageVector = if (activeReel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like reel",
                            tint = if (activeReel.isLiked) AuraPink else Color.White
                        )
                    }
                    Text(
                        text = "${activeReel.likesCount}",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    // Comments Button
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(46.dp)
                            .frostedGlass(CircleShape, useDarkTheme = true)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Comment,
                            contentDescription = "Reel comments",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "${activeReel.commentsCount}",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    // Share button
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(46.dp)
                            .frostedGlass(CircleShape, useDarkTheme = true)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share reel link",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Share",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Video Info overlay at bottom-left
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.70f)
                        .padding(bottom = 44.dp, start = 24.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AuraPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "aura_arts • Pranit",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = activeReel.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = activeReel.description,
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp)
                    )
                }

                // Vertical reel-switching navigation arrows (floating top center or side)
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .statusBarsPadding()
                        .padding(top = 12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Creation Feed",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Watch physical timelapses",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .statusBarsPadding()
                        .padding(top = 12.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = { if (activeIndex > 0) activeIndex-- },
                        modifier = Modifier
                            .frostedGlass(CircleShape, useDarkTheme = true)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Prev reel", tint = Color.White)
                    }
                    IconButton(
                        onClick = { if (activeIndex < reels.size - 1) activeIndex++ },
                        modifier = Modifier
                            .frostedGlass(CircleShape, useDarkTheme = true)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Next reel", tint = Color.White)
                    }
                }
            }
        }
    }
}
