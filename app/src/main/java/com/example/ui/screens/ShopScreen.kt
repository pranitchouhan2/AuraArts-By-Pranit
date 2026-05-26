package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Artwork
import com.example.ui.AppViewModel
import com.example.ui.components.BeautifulArtworkImagePlaceholder
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: AppViewModel,
    onNavigateToDetail: (String) -> Unit,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val artworks by viewModel.artworks.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    var showCartSheet by remember { mutableStateOf(false) }

    val itemsCount = remember(cartItems) {
        cartItems.sumOf { it.quantity }
    }

    val cartTotal = remember(cartItems) {
        cartItems.sumOf { it.price * it.quantity }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            // Elegant Store Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Aura Shop",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                        )
                    )
                    Text(
                        text = "Collect original pieces and custom panels",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f)
                        )
                    )
                }

                // Floatable basket trigger top right with count bubble
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(colors = listOf(AuraPurple, AuraCyan))
                        )
                        .clickable { showCartSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = "Open shopping bag",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    if (itemsCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .clip(CircleShape)
                                .background(AuraPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$itemsCount",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Shop Catalog Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Header offer Banner
                item(span = { GridItemSpan(2) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .frostedGlass(RoundedCornerShape(24.dp), useDarkTheme)
                                .background(
                                    Brush.linearGradient(
                                        colors = if (useDarkTheme) {
                                            listOf(AuraPurple.copy(alpha = 0.45f), AuraPink.copy(alpha = 0.4f))
                                        } else {
                                            listOf(AuraPurple.copy(alpha = 0.65f), AuraPink.copy(alpha = 0.6f))
                                        }
                                    )
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "LAUNCH EXCLUSIVE",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Complementary Shipping",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Get physical certificate of authenticity (COA) hand-signed by Pranit.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                            }
                        }
                    }
                }

                // Spotlight section partition heading
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Handmade Product Spotlight",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Catalog Grid items
                items(artworks) { item ->
                    ProductCard(
                        artwork = item,
                        onCardClick = { onNavigateToDetail(item.id) },
                        onAddToCart = {
                            viewModel.addToCart(item)
                            Toast.makeText(context, "Added to shopping bag!", Toast.LENGTH_SHORT).show()
                        },
                        useDarkTheme = useDarkTheme
                    )
                }
            }
        }

        // Beautiful Cart Bottom Overlay Sheet
        if (showCartSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCartSheet = false },
                containerColor = if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = AuraPurple
                            )
                            Text(
                                text = "Your Studio Bag",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        if (cartItems.isNotEmpty()) {
                            TextButton(onClick = { viewModel.clearCart() }) {
                                Text("Clear All", color = AuraPink, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Empty Bag",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your cart is blank",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray, fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    } else {
                        // Scrolling list of cart items
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            cartItems.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background((if (useDarkTheme) AuraBackgroundDark else AuraBackgroundLight).copy(alpha = 0.5f))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Thumbnail
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        BeautifulArtworkImagePlaceholder(
                                            artworkId = item.id,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Title & value
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "$${item.price} each",
                                            style = MaterialTheme.typography.bodySmall.copy(color = AuraPurple)
                                        )
                                    }

                                    // Quantity Controllers
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(AuraPurple.copy(alpha = 0.15f))
                                                .clickable { viewModel.decreaseQuantity(item.id) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Minus qty",
                                                tint = AuraPurple,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                        Text(
                                            text = "${item.quantity}",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(AuraPurple)
                                                .clickable { viewModel.increaseQuantity(item.id) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Plus qty",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Price Sum and Checkout
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "BAG SUB-TOTAL",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                                Text(
                                    text = "$$cartTotal",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AuraPurple,
                                        fontSize = 24.sp
                                    )
                                )
                            }

                            Button(
                                onClick = {
                                    showCartSheet = false
                                    // Direct checkout dispatching WhatsApp template
                                    val contactNum = "+919179999498"
                                    val orderItemsStr = cartItems.joinToString("\n") { "- ${it.title} (${it.quantity}x) - $${it.price}" }
                                    val formattedCheckoutMsg = "Hello Pranit, I would love to order these customized handmade art items from your Aura Arts shop:\n\n$orderItemsStr\n\nTotal price: $$cartTotal. Please guide me on bank deposit details and shipping timelines!"
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("https://api.whatsapp.com/send?phone=$contactNum&text=" + URLEncoder.encode(formattedCheckoutMsg, "UTF-8"))
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "WhatsApp error. Redirecting to mail...", Toast.LENGTH_SHORT).show()
                                        val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:pranitchouhan917@gmail.com")
                                            putExtra(Intent.EXTRA_SUBJECT, "Aura Arts Store Checkout")
                                            putExtra(Intent.EXTRA_TEXT, formattedCheckoutMsg)
                                        }
                                        context.startActivity(mailIntent)
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AuraPurple),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("Checkout Order", fontWeight = FontWeight.Bold)
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
            }
        }
    }
}

@Composable
fun ProductCard(
    artwork: Artwork,
    onCardClick: () -> Unit,
    onAddToCart: () -> Unit,
    useDarkTheme: Boolean
) {
    Card(
        onClick = onCardClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.3f)
            ) {
                BeautifulArtworkImagePlaceholder(
                    artworkId = artwork.id,
                    modifier = Modifier.fillMaxSize()
                )

                // Add to basket bottom overlay circle
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AuraPurple)
                        .clickable { onAddToCart() }
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Quick load item",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
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
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "$${artwork.price}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = AuraPurple,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "$${(artwork.price * 1.25).toInt()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }
            }
        }
    }
}
