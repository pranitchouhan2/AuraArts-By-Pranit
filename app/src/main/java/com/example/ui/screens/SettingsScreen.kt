package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current

    val currentDarkMode by viewModel.isDarkMode.collectAsState()
    val pushEnabled by viewModel.pushNotificationsEnabled.collectAsState()
    val orderEnabled by viewModel.orderUpdatesEnabled.collectAsState()
    val customAccent by viewModel.customThemeAccent.collectAsState()

    val accents = remember {
        listOf("Aura Classic", "Celestial Lavender", "Soft Blue", "Aesthetic Pink")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .frostedGlass(CircleShape, useDarkTheme)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Settings Back",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                }

                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )
            }

            // Options Scroll Container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Topic 1: Dynamic Theme Override
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.LightMode, contentDescription = null, tint = AuraPurple, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Display Style",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Dark Mode Row Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Dark Slate Theme", fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Subtle cosmic deep violet backgrounds",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }

                                Switch(
                                    checked = currentDarkMode == true,
                                    onCheckedChange = { viewModel.toggleDarkMode(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AuraPurple
                                    )
                                )
                            }
                        }
                    }
                }

                // Topic 2: Theme Accents Customizers
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.ColorLens, contentDescription = null, tint = AuraPurple, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Artistic Accents",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            accents.forEach { accent ->
                                val isSelected = accent == customAccent
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.setThemeAccent(accent)
                                            Toast.makeText(context, "$accent Set!", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = accent,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) AuraPurple else (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight)
                                        )
                                    )
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(AuraPurple)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Topic 3: Alerts Controls
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.NotificationsActive, contentDescription = null, tint = AuraPurple, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Aura Notifications",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Push toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Primary Push Toggles", fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Alert weekly drops of newest canvases",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }
                                Switch(
                                    checked = pushEnabled,
                                    onCheckedChange = { viewModel.togglePushNotifications(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AuraPurple
                                    )
                                )
                            }

                            Divider(modifier = Modifier.background(Color.Gray.copy(alpha = 0.2f)))

                            // Checkout order status toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Order Proposals Alerts", fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Updates on custom canvas approvals & booking statuses",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }
                                Switch(
                                    checked = orderEnabled,
                                    onCheckedChange = { viewModel.toggleOrderUpdates(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AuraPurple
                                    )
                                )
                            }
                        }
                    }
                }

                // Topic 4: About Studio
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = AuraPurple, modifier = Modifier.size(20.dp))
                        Text(
                            text = "About Aura Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Aura Arts",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AuraPurple)
                            )
                            Text(
                                text = "Dedicated to delivering high-fidelity spiritual paintings, pristine geometrical mandala sketches, and handcrafted clay models directly to your homes around the globe. Founded by Pranit Chouhan.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray, lineHeight = 20.sp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(modifier = Modifier.background(Color.Gray.copy(alpha = 0.2f)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Version Info", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                                Text(text = "1.0.0 (Play Store Ready)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }
        }
    }
}
