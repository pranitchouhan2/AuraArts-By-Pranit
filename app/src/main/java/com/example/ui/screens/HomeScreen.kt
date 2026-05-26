package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Artwork
import com.example.ui.AppViewModel
import com.example.ui.components.BeautifulArtworkImagePlaceholder
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToGallery: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCommissions: () -> Unit,
    onNavigateToReels: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    useDarkTheme: Boolean = false
) {
    val artworks by viewModel.artworks.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val reels by viewModel.reels.collectAsState()

    val unreadNotifsCount = notifications.count { !it.isRead }

    val categories = remember {
        listOf("Shiva Art", "Mandala Art", "Portrait Sketches", "Handmade Crafts", "Wall Decor")
    }

    // Filter featured artworks (e.g. ones with high price or specific ids)
    val featuredArtworks = remember(artworks) {
        artworks.filter { it.price > 300 }
    }

    val trendingArtworks = remember(artworks) {
        artworks.sortedByDescending { it.likesCount }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(modifier = Modifier.fillMaxSize()) {
            // Elegant Top Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Aura Arts",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Creative owner • Pranit Chouhan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f)
                        )
                    )
                }

                // Notification bell icon with unread badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .frostedGlass(CircleShape, useDarkTheme)
                        .clickable { onNavigateToNotifications() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications bell",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                        modifier = Modifier.size(24.dp)
                    )
                    if (unreadNotifsCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = (4).dp)
                                .clip(CircleShape)
                                .background(AuraPink)
                        )
                    }
                }
            }

            // Main scrollable canvas container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                // Horizontal category filters
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories) { cat ->
                        Box(
                            modifier = Modifier
                                .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme)
                                .clickable { onNavigateToGallery(cat) }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Featured artwork slider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Masterpieces Spotlight",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AuraPurple,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { onNavigateToGallery("All") }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredArtworks) { art ->
                        Card(
                            onClick = { onNavigateToDetail(art.id) },
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .width(280.dp)
                                .height(320.dp)
                                .frostedGlass(RoundedCornerShape(24.dp), useDarkTheme),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                BeautifulArtworkImagePlaceholder(
                                    artworkId = art.id,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Soft light overlay at bottom
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.35f)
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                            )
                                        )
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = art.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = art.category,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Book Custom Artwork Commission Banner
                Card(
                    onClick = { onNavigateToCommissions() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(24.dp), useDarkTheme)
                            .background(
                                Brush.linearGradient(
                                    colors = if (useDarkTheme) {
                                        listOf(AuraPurple.copy(alpha = 0.45f), AuraCyan.copy(alpha = 0.4f))
                                    } else {
                                        listOf(AuraPurple.copy(alpha = 0.65f), AuraCyan.copy(alpha = 0.6f))
                                    }
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Bespoke Spiritual Inquiries",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Book custom sized mandalas, Shiva art, & graphite sketches built perfectly to order.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { onNavigateToCommissions() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = AuraPurple
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Commission Now",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Trending artworks
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Creas",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(trendingArtworks) { art ->
                        Card(
                            onClick = { onNavigateToDetail(art.id) },
                            modifier = Modifier
                                .width(180.dp)
                                .height(220.dp)
                                .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    BeautifulArtworkImagePlaceholder(
                                        artworkId = art.id,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Likes counter Badge
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black.copy(alpha = 0.45f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Popularity",
                                                tint = AuraPink,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Text(
                                                text = "${art.likesCount}",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }

                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = art.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "$${art.price}",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = AuraPurple,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Reels preview strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Creation Reels",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Watch Feed",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AuraPurple,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { onNavigateToReels() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(reels) { reel ->
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(140.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = when (reel.thumbnailColor) {
                                            "purple" -> listOf(AuraPurple, SecondaryPurple)
                                            "blue" -> listOf(TertiaryBlue, AuraCyan)
                                            else -> listOf(AuraPink, AuraPurple)
                                        }
                                    )
                                )
                                .clickable { onNavigateToReels() },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Reel preview",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = reel.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
