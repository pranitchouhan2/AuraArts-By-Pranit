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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.Artwork
import com.example.ui.AppViewModel
import com.example.ui.components.BeautifulArtworkImagePlaceholder
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: AppViewModel,
    onNavigateToDetail: (String) -> Unit,
    useDarkTheme: Boolean = false
) {
    val filteredArtworks by viewModel.filteredArtworks.collectAsState()
    val searchQuery by viewModel.gallerySearchQuery.collectAsState()
    val selectedCategory by viewModel.gallerySelectedCategory.collectAsState()

    val categories = remember {
        listOf("All", "Shiva Art", "Mandala Art", "Portrait Sketches", "Handmade Crafts", "Wall Decor")
    }

    // Fullscreen Artwork Preview dialog state
    var previewArtwork by remember { mutableStateOf<Artwork?>(null) }

    // Double Column Masonry Distribution
    val column1Items = remember(filteredArtworks) {
        filteredArtworks.filterIndexed { index, _ -> index % 2 == 0 }
    }
    val column2Items = remember(filteredArtworks) {
        filteredArtworks.filterIndexed { index, _ -> index % 2 != 0 }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            // Elegant Editorial Heading
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text(
                    text = "Aesthetic Vault",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )
                Text(
                    text = "Browse experimental hand-drawn creations",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.62f)
                    )
                )
            }

            // Modern Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateGallerySearch(it) },
                placeholder = { Text("Search mandala, Shiva themes, watercolors...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.7f),
                    unfocusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AuraPurple
                    )
                },
                singleLine = true
            )

            // Category Chips lazy horizontal slider
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat.equals(selectedCategory, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.horizontalGradient(colors = listOf(AuraPurple, AuraCyan))
                                        )
                                } else {
                                    Modifier.frostedGlass(RoundedCornerShape(16.dp), useDarkTheme)
                                }
                            )
                            .clickable { viewModel.updateGalleryCategory(cat) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) Color.White else (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Dual Column Masonry Body
            if (filteredArtworks.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No artworks found",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Try refining your search keyword",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Left Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        column1Items.forEach { art ->
                            MasonryCard(
                                artwork = art,
                                onCardClick = { onNavigateToDetail(art.id) },
                                onCardLongClick = { previewArtwork = art },
                                onLikeClick = { viewModel.toggleLikeArtwork(art.id, art.isLiked) },
                                onSaveClick = { viewModel.toggleSaveArtwork(art.id, art.isSaved) },
                                height = getMasonryHeight(art.id),
                                useDarkTheme = useDarkTheme
                            )
                        }
                    }

                    // Right Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        column2Items.forEach { art ->
                            MasonryCard(
                                artwork = art,
                                onCardClick = { onNavigateToDetail(art.id) },
                                onCardLongClick = { previewArtwork = art },
                                onLikeClick = { viewModel.toggleLikeArtwork(art.id, art.isLiked) },
                                onSaveClick = { viewModel.toggleSaveArtwork(art.id, art.isSaved) },
                                height = getMasonryHeight(art.id),
                                useDarkTheme = useDarkTheme
                            )
                        }
                    }
                }
            }
        }

        // Fullscreen Preview Zoom Dialog Overlay (Pinterest style)
        previewArtwork?.let { art ->
            Dialog(
                onDismissRequest = { previewArtwork = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .wrapContentHeight()
                            .frostedGlass(RoundedCornerShape(32.dp), useDarkTheme)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = art.category.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = AuraPurple,
                                    letterSpacing = 2.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            IconButton(onClick = { previewArtwork = null }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close overlay",
                                    tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                                )
                            }
                        }

                        // Image Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .clip(RoundedCornerShape(24.dp))
                        ) {
                            BeautifulArtworkImagePlaceholder(
                                artworkId = art.id,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = art.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = art.story,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.7f)
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    previewArtwork = null
                                    onNavigateToDetail(art.id)
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AuraPurple)
                            ) {
                                Text("Aesthetic Story", fontWeight = FontWeight.Bold)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = { viewModel.toggleLikeArtwork(art.id, art.isLiked) }) {
                                    Icon(
                                        imageVector = if (art.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like art",
                                        tint = if (art.isLiked) AuraPink else Color.Gray
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleSaveArtwork(art.id, art.isSaved) }) {
                                    Icon(
                                        imageVector = if (art.isSaved) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Save art",
                                        tint = if (art.isSaved) AuraPurple else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MasonryCard(
    artwork: Artwork,
    onCardClick: () -> Unit,
    onCardLongClick: () -> Unit,
    onLikeClick: () -> Unit,
    onSaveClick: () -> Unit,
    height: Int,
    useDarkTheme: Boolean
) {
    Card(
        onClick = onCardClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                BeautifulArtworkImagePlaceholder(
                    artworkId = artwork.id,
                    modifier = Modifier.fillMaxSize()
                )

                // Save bookmarks overlay top-right
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable { onSaveClick() }
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (artwork.isSaved) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save shortcut",
                        tint = if (artwork.isSaved) AuraPurple else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Light quick zoom triggers overlay
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { onCardLongClick() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Text("Pre", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${artwork.price}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AuraPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Likes click actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.clickable { onLikeClick() }
                    ) {
                        Icon(
                            imageVector = if (artwork.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like icon",
                            tint = if (artwork.isLiked) AuraPink else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${artwork.likesCount}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (useDarkTheme) AuraOnBackgroundDark.copy(alpha = 0.6f) else AuraOnBackgroundLight.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    }
}

// Procedural dynamic heights to simulate gorgeous Pinterest masonry
fun getMasonryHeight(id: String): Int {
    return when (id) {
        "shiva_1" -> 250
        "shiva_2" -> 210
        "mandala_1" -> 260
        "mandala_2" -> 200
        "portrait_1" -> 230
        "portrait_2" -> 210
        "craft_1" -> 240
        else -> 220
    }
}
