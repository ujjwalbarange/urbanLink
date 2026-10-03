package com.nagpur.connect.ui.screens.emergency

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

data class EmergencyContact(
    val service: String,
    val number: String,
    val description: String,
    val icon: String
)

val EMERGENCY_CONTACTS = listOf(
    EmergencyContact(
        service = "Police",
        number = "112",
        description = "National emergency helpline for immediate police assistance",
        icon = "👮"
    ),
    EmergencyContact(
        service = "Fire Brigade",
        number = "101",
        description = "Fire suppression and structural rescue emergency services",
        icon = "🚒"
    ),
    EmergencyContact(
        service = "Ambulance",
        number = "108",
        description = "Emergency medical services and patient transport",
        icon = "🚑"
    ),
    EmergencyContact(
        service = "Disaster Management",
        number = "1078",
        description = "National disaster response and relief cell",
        icon = "🌊"
    )
)

@Composable
fun EmergencyHubScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ──────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicTheme.colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Emergency Help",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Warning Banner (Red) ─────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.criticalBg)
                .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CivicTheme.colors.critical.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Alert",
                        tint = CivicTheme.colors.critical,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Immediate Danger?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.critical
                    )
                    Text(
                        text = "If you or someone is in immediate danger, please call the emergency services directly below.",
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textSecondary,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Disclaimer Box (Yellow) ──────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CivicTheme.colors.mediumBg)
                .border(1.dp, CivicTheme.colors.mediumBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = "Notice",
                    tint = CivicTheme.colors.medium,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Important: Displaying these verified emergency contacts does not mean services have been dispatched. You must tap to call directly.",
                    fontSize = 11.sp,
                    color = CivicTheme.colors.medium,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "VERIFIED EMERGENCY CONTACTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textTertiary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ── 4 Emergency Contacts ─────────────────────────────
        EMERGENCY_CONTACTS.forEach { contact ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.surface0)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CivicTheme.colors.surface2),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = contact.icon, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = contact.service,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicTheme.colors.textPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${contact.number}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicTheme.colors.accent
                                )
                            }
                            Text(
                                text = contact.description,
                                fontSize = 11.sp,
                                color = CivicTheme.colors.textTertiary,
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.number}"))
                            context.startActivity(dialIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.critical),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Call",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
