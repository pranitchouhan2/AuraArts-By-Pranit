package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppViewModel
import com.example.ui.components.BeautifulArtworkImagePlaceholder
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*

data class ReviewData(
    val author: String,
    val text: String,
    val stars: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onNavigateToGallery: (String) -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToSettings: () -> Unit,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val artworks by viewModel.artworks.collectAsState()

    val reviews = remember {
        listOf(
            ReviewData("Abhishek Sharma", "Ordered the custom sahastrara lotus mandala. Divine energy, exquisite framing and packaging!", 5),
            ReviewData("Pooja Chouhan", "The graphite charcoal portrait of Shiva is serene. Absolutely breathtaking!", 5),
            ReviewData("Rohan Mehta", "Extremely neat work and detailed micro-patterns. Communication with Pranit was fantastic.", 5)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Profile top row header (settings button inside)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aura Studio",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )

                IconButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .frostedGlass(CircleShape, useDarkTheme)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Open Settings",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                }
            }

            // Creator Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar with decorative concentric gradients
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .border(
                            2.5.dp,
                            Brush.sweepGradient(colors = listOf(AuraPurple, AuraPink, AuraCyan, AuraPurple)),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(AuraLavender),
                    contentAlignment = Alignment.Center
                ) {
                    // Standard custom drawable icon for Aura
                    Image(
                        painter = painterResource(id = R.drawable.aura_logo_icon),
                        contentDescription = "Pranit Chouhan Profile Pic",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pranit Chouhan",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                        fontSize = 24.sp
                    )
                )

                Text(
                    text = "Founder & Artist • @_aura__arts",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AuraPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Stats Dashboard Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "14.2K",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Followers",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color.Gray.copy(alpha = 0.4f))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "480",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color.Gray.copy(alpha = 0.4f))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "48",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Canvases",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Artist Biography Description
                Text(
                    text = "Bespoke handmade spiritual canvases, giant sacred geometric mandalas, charcoal portrait sketches and wall energy panels. Delivering worldwide.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.75f),
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Instagram & Contact quick buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/_aura__arts"))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Redirecting to instagram web...", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuraPurple)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Instagram Feed", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = onNavigateToContact,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AuraPurple.copy(alpha = 0.15f),
                            contentColor = AuraPurple
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Direct Connect", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Achievement Badges Row section
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text(
                    text = "Aura Badges",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BadgeChip(
                        name = "Veri Handmade",
                        desc = "100% Genuine Crafts",
                        color = AuraPurple,
                        useDarkTheme = useDarkTheme
                    )
                    BadgeChip(
                        name = "Mandala Master",
                        desc = "Radial geometric grid draft",
                        color = AuraPink,
                        useDarkTheme = useDarkTheme
                    )
                    BadgeChip(
                        name = "Celestial Art",
                        desc = "Shiva canvas pioneer",
                        color = AuraCyan,
                        useDarkTheme = useDarkTheme
                    )
                }
            }

            // Customer Reviews Carousel section
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text(
                    text = "Customer Review",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reviews) { r ->
                        Card(
                            modifier = Modifier
                                .width(280.dp)
                                .height(130.dp)
                                .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "\"${r.text}\"",
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "- ${r.author}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        for (i in 0 until r.stars) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = AuraPink,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Featured Highlights category portfolio buttons
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text(
                    text = "Portfolio Highlights",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onNavigateToGallery("Shiva Art") }
                    ) {
                        BeautifulArtworkImagePlaceholder(artworkId = "shiva_1", modifier = Modifier.fillMaxSize())
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Shiva Series", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onNavigateToGallery("Mandala Art") }
                    ) {
                        BeautifulArtworkImagePlaceholder(artworkId = "mandala_1", modifier = Modifier.fillMaxSize())
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Mandala Geometry", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeChip(
    name: String,
    desc: String,
    color: Color,
    useDarkTheme: Boolean
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .frostedGlass(RoundedCornerShape(16.dp), useDarkTheme)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = Color.Gray),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
