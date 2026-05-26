package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.WatercolorBackground
import com.example.ui.theme.*
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    onBackClick: () -> Unit,
    useDarkTheme: Boolean = false
) {
    val context = LocalContext.current

    // Contact Form States
    var clientName by remember { mutableStateOf("") }
    var clientEmail by remember { mutableStateOf("") }
    var clientText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        WatercolorBackground(useDarkTheme = useDarkTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header back Row
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
                        .clip(CircleShape)
                        .background((if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Contact Back",
                        tint = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                }

                Text(
                    text = "Aura Studio Connect",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight
                    )
                )
            }

            // Scroll container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                // Intro text
                Column {
                    Text(
                        text = "Connect Directly",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight,
                            fontSize = 28.sp
                        )
                    )
                    Text(
                        text = "Feel free to leave a message. Pranit typically responds within a couple of hours!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = (if (useDarkTheme) AuraOnBackgroundDark else AuraOnBackgroundLight).copy(alpha = 0.6f)
                        )
                    )
                }

                // Glassmorphism Quick channel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // WhatsApp Glass Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(AuraPurple.copy(alpha = 0.15f), AuraCyan.copy(alpha = 0.15f))
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                            .clickable {
                                val textEncoded = URLEncoder.encode("Hello Pranit, I found you on Aura Arts. I would love to connect!", "UTF-8")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+919179999498&text=$textEncoded"))
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp error.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Message, contentDescription = "WhatsApp", tint = AuraPurple, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("WhatsApp", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text("+91 91799", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = Color.Gray)
                        }
                    }

                    // Email Glass Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(AuraPink.copy(alpha = 0.15f), AuraPurple.copy(alpha = 0.15f))
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                            .clickable {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:pranitchouhan917@gmail.com"))
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Email is not configured.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = AuraPink, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Email", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text("pranitchouhan917", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = Color.Gray)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp).background(Color.Gray.copy(alpha = 0.2f)))

                // Quick Contact Form Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = (if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight).copy(alpha = 0.6f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Direct Inquiry Form",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        // Client Name Form
                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Client Email Form
                        OutlinedTextField(
                            value = clientEmail,
                            onValueChange = { clientEmail = it },
                            label = { Text("Your Email Contact") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Message Form
                        OutlinedTextField(
                            value = clientText,
                            onValueChange = { clientText = it },
                            label = { Text("Message / Custom Query") },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 4
                        )

                        Button(
                            onClick = {
                                if (clientName.isBlank() || clientEmail.isBlank() || clientText.isBlank()) {
                                    Toast.makeText(context, "Please complete the query formulation.", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                Toast.makeText(context, "Message saved. Launching client dispatcher...", Toast.LENGTH_SHORT).show()

                                // Send over Email / WhatsApp
                                val contactNum = "+919179999498"
                                val formattedFormMsg = "Hello Pranit Chouhan! Here is a direct connect message from your Aura Arts App:\n\n- Sender Name: $clientName\n- Contact Detail: $clientEmail\n- Message: $clientText"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$contactNum&text=" + URLEncoder.encode(formattedFormMsg, "UTF-8")))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:pranitchouhan917@gmail.com")
                                        putExtra(Intent.EXTRA_SUBJECT, "Aura Arts App Direct Inquiry")
                                        putExtra(Intent.EXTRA_TEXT, formattedFormMsg)
                                    }
                                    context.startActivity(mailIntent)
                                }

                                clientName = ""
                                clientEmail = ""
                                clientText = ""
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraPurple),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message", modifier = Modifier.size(16.dp))
                                Text("Send Query Directly", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
