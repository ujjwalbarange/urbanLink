package com.nagpur.connect.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun CitizenDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onTrackReport: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var trackInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(CivicTheme.colors.surface0)
    ) {
        // Drawer Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .border(width = 0.5.dp, color = CivicTheme.colors.border)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Menu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.accent
            )
            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = CivicTheme.colors.textSecondary
                )
            }
        }

        // Citizen Identity Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CivicTheme.colors.accentMuted),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.accent
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Citizen User",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.textPrimary
                )
                Text(
                    text = "Nagpur, Maharashtra",
                    fontSize = 12.sp,
                    color = CivicTheme.colors.textTertiary
                )
            }
        }

        HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)

        // Nav items
        Spacer(modifier = Modifier.height(8.dp))
        DrawerNavItem(
            label = "Dashboard",
            icon = Icons.Outlined.Home,
            selected = currentRoute == "home",
            onClick = {
                onNavigate("home")
                onCloseDrawer()
            }
        )
        DrawerNavItem(
            label = "My Reports",
            icon = Icons.Outlined.Assignment,
            selected = currentRoute == "my_reports",
            onClick = {
                onNavigate("my_reports")
                onCloseDrawer()
            }
        )
        DrawerNavItem(
            label = "Emergency Help",
            icon = Icons.Outlined.Warning,
            selected = currentRoute == "emergency",
            onClick = {
                onNavigate("emergency")
                onCloseDrawer()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Emergency Call 112 Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CivicTheme.colors.criticalBg)
                .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "🚨 Emergency?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.critical
                )
                Text(
                    text = "Call 112 for immediate police, fire or medical dispatch",
                    fontSize = 11.sp,
                    color = CivicTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                        context.startActivity(dialIntent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.critical),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(text = "Call 112", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Track a Report Input Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CivicTheme.colors.surface1)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = "Track a Report",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = trackInput,
                        onValueChange = { trackInput = it },
                        placeholder = { Text("NAG-2026-XXXX", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CivicTheme.colors.surface0,
                            unfocusedContainerColor = CivicTheme.colors.surface0,
                            focusedBorderColor = CivicTheme.colors.accent,
                            unfocusedBorderColor = CivicTheme.colors.border
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            if (trackInput.isNotBlank()) {
                                onTrackReport(trackInput.trim())
                                onCloseDrawer()
                            }
                        })
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            if (trackInput.isNotBlank()) {
                                onTrackReport(trackInput.trim())
                                onCloseDrawer()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Text("Go", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, CivicTheme.colors.border)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Urban Link v1.0 — AI Civic Response",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = CivicTheme.colors.textTertiary
            )
        }
    }
}

@Composable
private fun DrawerNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (selected) CivicTheme.colors.accentMuted else Color.Transparent
    val contentColor = if (selected) CivicTheme.colors.accent else CivicTheme.colors.textSecondary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(bgColor)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
    }
}
