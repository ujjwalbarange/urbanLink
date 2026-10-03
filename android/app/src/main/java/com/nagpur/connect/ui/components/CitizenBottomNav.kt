package com.nagpur.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun CitizenBottomNav(
    currentRoute: String,
    onNavigateHome: () -> Unit,
    onNavigateReports: () -> Unit,
    onStartVoice: () -> Unit,
    onNavigateEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(CivicTheme.colors.surface0)
                .border(width = 0.5.dp, color = CivicTheme.colors.border)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Tab 1: Home
            BottomNavItem(
                icon = if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home,
                label = "Home",
                isSelected = currentRoute == "home",
                onClick = onNavigateHome
            )

            // Tab 2: Reports
            BottomNavItem(
                icon = if (currentRoute == "my_reports") Icons.Filled.Assignment else Icons.Outlined.Assignment,
                label = "Reports",
                isSelected = currentRoute == "my_reports",
                onClick = onNavigateReports
            )

            // Tab 3: Center Mic / Voice Report Action
            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                CivicTheme.colors.primaryContainer,
                                CivicTheme.colors.accent
                            )
                        )
                    )
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .clickable { onStartVoice() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Report",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Tab 4: Emergency SOS
            BottomNavItem(
                icon = if (currentRoute == "emergency") Icons.Filled.Warning else Icons.Outlined.Warning,
                label = "SOS 112",
                isSelected = currentRoute == "emergency",
                isEmergency = true,
                onClick = onNavigateEmergency
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    isEmergency: Boolean = false,
    onClick: () -> Unit
) {
    val tint = when {
        isEmergency -> CivicTheme.colors.critical
        isSelected -> CivicTheme.colors.accent
        else -> CivicTheme.colors.textTertiary
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = tint,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
