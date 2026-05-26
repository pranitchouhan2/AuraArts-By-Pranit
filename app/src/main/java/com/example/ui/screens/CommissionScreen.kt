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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.AppViewModel
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommissionScreen(
    viewModel: AppViewModel,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val inquiries by viewModel.inquiries.collectAsState()

    var showHistoryDialog by remember { mutableStateOf(false) }

    // Inputs States
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val styleOptions = remember {
        listOf("Shiva Cosmic Art", "Mandala Healing Geometry", "Realistic Portrait Sketch", "Handmade Resin Craft", "Wall Decor Canvas")
    }
    var selectedStyle by remember { mutableStateOf("Shiva Cosmic Art") }

    val sizeOptions = remember {
        listOf("A4 Frame (8.3 x 11.7 in)", "A3 Master (11.7 x 16.5 in)", "Square Stretched Canvas (12 x 12 in)", "Expanded Canvas (24 x 36 in)", "Bespoke Custom Border")
    }
    var selectedSize by remember { mutableStateOf("A4 Frame (8.3 x 11.7 in)") }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 12.dp)
        ) {
            // Screen Title Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Bespoke Commission",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                        )
                    )
                    Text(
                        text = "Collaborate directly with Pranit",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f)
                        )
                    )
                }

                // Inquiry history button
                Button(
                    onClick = { showHistoryDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuraPurple.copy(alpha = 0.15f),
                        contentColor = AuraPurple
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Logs (${inquiries.size})",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Input Fields Scroll Container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Interactive Intro block
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .frostedGlass(RoundedCornerShape(20.dp), useDarkTheme),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AuraPurple.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Draw,
                                contentDescription = null,
                                tint = AuraPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "How it Works",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Describe your concept, budget, & style. We save it in offline logs and trigger a custom WhatsApp draft.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }
                }

                // Name field
                Column {
                    Text(
                        text = "Your Full Name",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Let Pranit know who he's chatting with...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.5f),
                            unfocusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.3f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                // Selection for Art Style
                Column {
                    Text(
                        text = "Select Art Style",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(styleOptions) { style ->
                            val active = style == selectedStyle
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (active) {
                                            Modifier
                                                .clip(RoundedCornerShape(18.dp))
                                                .background(Brush.horizontalGradient(listOf(AuraPurple, AuraCyan)))
                                        } else {
                                            Modifier.frostedGlass(RoundedCornerShape(18.dp), useDarkTheme)
                                        }
                                    )
                                    .clickable { selectedStyle = style }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = style,
                                    color = if (active) Color.White else (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight),
                                    fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Selection for Canvas Size
                Column {
                    Text(
                        text = "Dimension & Size",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(sizeOptions) { sizeVal ->
                            val active = sizeVal == selectedSize
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (active) {
                                            Modifier
                                                .clip(RoundedCornerShape(18.dp))
                                                .background(Brush.horizontalGradient(listOf(AuraPurple, AuraPink)))
                                        } else {
                                            Modifier.frostedGlass(RoundedCornerShape(18.dp), useDarkTheme)
                                        }
                                    )
                                    .clickable { selectedSize = sizeVal }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = sizeVal,
                                    color = if (active) Color.White else (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight),
                                    fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Description field
                Column {
                    Text(
                        text = "Spiritual Concept / Custom Story",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("E.g. I want a cosmic pastel watercolor canvas of Shiva Tandava with customized glowing mandala rays circles around his crown...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.5f),
                            unfocusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.3f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        maxLines = 5
                    )
                }

                // Budget field
                Column {
                    Text(
                        text = "Expected Budget ($ or INR)",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = budget,
                        onValueChange = { budget = it },
                        placeholder = { Text("E.g. $400 or ₹25,000") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.5f),
                            unfocusedContainerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.3f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Submit Action Button
                Button(
                    onClick = {
                        if (name.isBlank() || description.isBlank() || budget.isBlank()) {
                            Toast.makeText(context, "Please complete all inputs first.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Save offline Room first
                        viewModel.submitCommissionInquiry(description, selectedStyle, selectedSize, budget, name)
                        Toast.makeText(context, "Proposal saved offline! Directing to chat...", Toast.LENGTH_SHORT).show()

                        // Direct WhatsApp Send
                        val contactNum = "+919179999498"
                        val customMessage = "Hello Pranit Chouhan! I would love to commission a custom art. Here are my proposal details:\n\n- Name: $name\n- Requested Style: $selectedStyle\n- Dimensions: $selectedSize\n- Expected Budget: $budget\n- Concept Story: $description\n\nPlease let me know your thoughts and review my sketch design!"
                        try {
                            val msgEncoded = URLEncoder.encode(customMessage, "UTF-8")
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://api.whatsapp.com/send?phone=$contactNum&text=$msgEncoded")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:pranitchouhan917@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Aura Arts Bespoke Commission Proposal")
                                putExtra(Intent.EXTRA_TEXT, customMessage)
                            }
                            context.startActivity(mailIntent)
                        }

                        // Reset inputs
                        description = ""
                        budget = ""
                        name = ""
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AuraPurple),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Send Custom Proposal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        }

        // History Log Dialog
        if (showHistoryDialog) {
            Dialog(
                onDismissRequest = { showHistoryDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.75f)
                            .clip(RoundedCornerShape(28.dp))
                            .background(if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight)
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Commission Logs",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            IconButton(onClick = { showHistoryDialog = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close history"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (inquiries.isEmpty()) {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No historic proposal logs stored offline.", color = Color.Gray)
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                inquiries.forEach { inq ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background((if (useDarkTheme) AuraBackgroundDark else AuraBackgroundLight).copy(alpha = 0.5f))
                                            .padding(16.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = inq.artStyle,
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = AuraPurple)
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(AuraCyan.copy(alpha = 0.2f))
                                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = inq.status,
                                                        style = MaterialTheme.typography.labelSmall.copy(color = AuraPurple, fontWeight = FontWeight.Bold)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Size: ${inq.size} | Budget: ${inq.budget}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = inq.description,
                                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
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
}
