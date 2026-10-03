package com.nagpur.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun CitizenTopAppBar(
    onMenuClick: () -> Unit,
    onEmergencyClick: () -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(CivicTheme.colors.surface0)
            .border(width = 0.5.dp, color = CivicTheme.colors.border)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Menu + Urban Link Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onTitleClick() }
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = CivicTheme.colors.textPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Logo mark
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                CivicTheme.colors.accent,
                                CivicTheme.colors.primaryContainer
                            )
                        )
                    )
                    .shadow(2.dp, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "UL",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Urban Link",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary,
                    letterSpacing = (-0.3).sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.surface1)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.secondary)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Nagpur NMC",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.textSecondary
                    )
                }
            }
        }

        // Right: Emergency SOS badge + Notification Bell + Citizen Profile Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // SOS quick trigger
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CivicTheme.colors.criticalBg)
                    .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(999.dp))
                    .clickable { onEmergencyClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚨", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "112",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.critical
                    )
                }
            }

            // Notification Bell with badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { /* Notification drawer or trigger */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = CivicTheme.colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.critical),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "3",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Citizen Avatar with presence status
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(CivicTheme.colors.accentMuted)
                    .border(1.dp, CivicTheme.colors.border, CircleShape)
                    .clickable { onMenuClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.accent
                )
                // Live green dot
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.secondary)
                        .border(1.5.dp, CivicTheme.colors.surface0, CircleShape)
                )
            }
        }
    }
}
