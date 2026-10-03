package com.nagpur.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
            .height(56.dp)
            .background(CivicTheme.colors.surface0)
            .border(width = 0.5.dp, color = CivicTheme.colors.border)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Hamburger menu
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = CivicTheme.colors.textPrimary
            )
        }

        // Center: Nagpur Connect brand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onTitleClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CivicTheme.colors.accentMuted)
                    .border(0.5.dp, CivicTheme.colors.border, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏛️",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Nagpur Connect",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.accent,
                letterSpacing = (-0.3).sp
            )
        }

        // Right: Emergency SOS icon & Notification bell
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onEmergencyClick) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(CivicTheme.colors.criticalBg, CircleShape)
                        .border(1.dp, CivicTheme.colors.criticalBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🚨", fontSize = 14.sp)
                }
            }
        }
    }
}
