package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Artwork
import com.example.ui.AppViewModel
import com.example.ui.components.BeautifulArtworkImagePlaceholder
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import java.net.URLEncoder

@Composable
fun DetailScreen(
    artworkId: String,
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val artworks by viewModel.artworks.collectAsState()

    val artwork = remember(artworkId, artworks) {
        artworks.find { it.id == artworkId } ?: artworks.firstOrNull()
    }

    if (artwork == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading artwork...")
        }
        return
    }

    // Filter recommendations by same category or same creative vibe
    val recommendations = remember(artwork, artworks) {
        artworks.filter { it.id != artwork.id && it.category == artwork.category }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            // Screen Header Back / Action row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .frostedGlass(CircleShape, useDarkTheme)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                }

                Text(
                    text = "Artwork Story",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )

                IconButton(
                    onClick = {
                        // Instagram share mock
                        Toast.makeText(context, "Link copied! Share it directly to Instagram.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .frostedGlass(CircleShape, useDarkTheme)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share on social media",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                }
            }

            // Scrollable canvas description details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                // Main High definition artwork representation card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    BeautifulArtworkImagePlaceholder(
                        artworkId = artwork.id,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Details details card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = artwork.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AuraPurple,
                                letterSpacing = 2.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconButton(onClick = { viewModel.toggleLikeArtwork(artwork.id, artwork.isLiked) }) {
                                Icon(
                                    imageVector = if (artwork.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like art toggle",
                                    tint = if (artwork.isLiked) AuraPink else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = artwork.title,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Key structural summary attributes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Materials Used Badges
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .frostedGlass(RoundedCornerShape(16.dp), useDarkTheme)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "CREATION TIME",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.Gray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = artwork.creationTime,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Creation Time Badge
                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .frostedGlass(RoundedCornerShape(16.dp), useDarkTheme)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "MATERIALS USED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.Gray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = artwork.materials,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Detailed physical/spiritual story explanation block
                    Text(
                        text = "THE CREATIVE INSIGHT",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AuraPurple,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = artwork.story,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.82f),
                            lineHeight = 25.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Purchase & Inquire panel row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(24.dp), useDarkTheme)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(AuraPurple.copy(alpha = 0.12f), AuraCyan.copy(alpha = 0.12f))
                                )
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ORIGINAL VAL",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                            )
                            Text(
                                text = "$${artwork.price}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AuraPurple
                                )
                            )
                        }

                        Button(
                            onClick = {
                                // Direct order WhatsApp query setup
                                val contactNum = "+919179999498"
                                val formattedMsg = "Hello Pranit, I found your gorgeous artwork \"${artwork.title}\" on your Aura Arts app. I am highly interested in inquiring on custom sizing or booking this original creation! Can you brief me on details?"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://api.whatsapp.com/send?phone=$contactNum&text=" + URLEncoder.encode(formattedMsg, "UTF-8"))
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp is not installed. Sending mail...", Toast.LENGTH_SHORT).show()
                                    val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:pranitchouhan917@gmail.com")
                                        putExtra(Intent.EXTRA_SUBJECT, "Aura Arts Original Booking: ${artwork.title}")
                                        putExtra(Intent.EXTRA_TEXT, formattedMsg)
                                    }
                                    context.startActivity(mailIntent)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraPurple),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Inquire Booking", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Horizontal scrolling adjacency recommendation cards
                    if (recommendations.isNotEmpty()) {
                        Text(
                            text = "Complementary Artworks",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(recommendations) { rec ->
                                Card(
                                    onClick = { onNavigateToDetail(rec.id) },
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(180.dp)
                                        .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                                    shape = RoundedCornerShape(20.dp),
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
                                                artworkId = rec.id,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        Text(
                                            text = rec.title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(8.dp)
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
}
