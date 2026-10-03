package com.nagpur.connect.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.ActiveReportModel
import com.nagpur.connect.ui.components.SeverityBadge
import com.nagpur.connect.ui.components.StatusBadge
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun CitizenHomeScreen(
    activeReports: List<ActiveReportModel>,
    onSelectDepartment: (String) -> Unit,
    onStartTextCompose: () -> Unit,
    onStartVoiceCompose: () -> Unit,
    onViewReport: (String) -> Unit,
    onViewAllReports: () -> Unit,
    onNavigateTrackId: () -> Unit = {},
    onNavigateEmergency: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // ── 1. Top Civic Context & Location Pill ─────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(CivicTheme.colors.surface1)
                            .border(0.5.dp, CivicTheme.colors.border, RoundedCornerShape(999.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = CivicTheme.colors.accent,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nagpur • Ward 14 (Dharampeth)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }

                    // Civic Grid Status
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFECFDF5))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CivicTheme.colors.secondary)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Civic Grid Normal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.secondary
                        )
                    }
                }
            }

            // ── 2. Personalized Greeting ─────────────────────────
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Good morning, Ujjwal",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CivicTheme.colors.textPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Citizen",
                            tint = CivicTheme.colors.accent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Your voice is actively making Nagpur cleaner, safer, and smarter.",
                        fontSize = 13.sp,
                        color = CivicTheme.colors.textSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // ── 3. Hero Card: Civic Gateway ──────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFDBE1FF),
                                    CivicTheme.colors.surface0,
                                    CivicTheme.colors.surface1
                                )
                            )
                        )
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(20.dp))
                        .shadow(2.dp, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(CivicTheme.colors.accentMuted)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "NMC CITIZEN PORTAL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CivicTheme.colors.accent,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "How can we help improve your city?",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicTheme.colors.textPrimary,
                                    lineHeight = 25.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CivicTheme.colors.surface0)
                                    .shadow(2.dp, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏛️", fontSize = 24.sp)
                            }
                        }

                        // CTA Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Primary Report Button
                            Button(
                                onClick = onStartTextCompose,
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CivicTheme.colors.accent
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Report Issue",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Secondary Tap to Speak Button
                            Button(
                                onClick = onStartVoiceCompose,
                                modifier = Modifier
                                    .weight(1.1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CivicTheme.colors.surface0
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CivicTheme.colors.border),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(CivicTheme.colors.critical)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = CivicTheme.colors.accent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Speak",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CivicTheme.colors.textPrimary
                                )
                            }
                        }
                    }
                }
            }

            // ── 4. Quick Navigation (2x2 Grid) ───────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "QUICK NAVIGATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textTertiary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Instant Access",
                            fontSize = 11.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "Report Issue",
                            subtitle = "Quick 1-tap entry",
                            icon = Icons.Default.AddAlert,
                            iconBg = Color(0xFFDBE1FF),
                            iconTint = CivicTheme.colors.accent,
                            onClick = onStartTextCompose,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionCard(
                            title = "My Reports",
                            subtitle = "${activeReports.size} active",
                            icon = Icons.Default.Assignment,
                            iconBg = Color(0xFF6CF8BB),
                            iconTint = CivicTheme.colors.secondary,
                            onClick = onViewAllReports,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "Track ID",
                            subtitle = "Real-time status",
                            icon = Icons.Default.Radar,
                            iconBg = Color(0xFFE2E7FF),
                            iconTint = CivicTheme.colors.accent,
                            onClick = onNavigateTrackId,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionCard(
                            title = "Emergency 112",
                            subtitle = "Instant Help",
                            icon = Icons.Default.Call,
                            iconBg = CivicTheme.colors.critical,
                            iconTint = Color.White,
                            isEmergency = true,
                            onClick = onNavigateEmergency,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ── 5. Civic Performance Stats Bar ───────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CivicTheme.colors.surface0)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatColumn(number = "4,821", label = "Resolved")
                    Box(modifier = Modifier.size(1.dp, 28.dp).background(CivicTheme.colors.border))
                    StatColumn(number = "4.2h", label = "Avg Time")
                    Box(modifier = Modifier.size(1.dp, 28.dp).background(CivicTheme.colors.border))
                    StatColumn(number = "94%", label = "Resolution")
                }
            }

            // ── 6. Active Reports Section ─────────────────────────
            if (activeReports.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ACTIVE REPORTS (${activeReports.size})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textTertiary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "View all →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicTheme.colors.accent,
                                modifier = Modifier.clickable { onViewAllReports() }
                            )
                        }

                        activeReports.take(3).forEach { report ->
                            StitchActiveReportCard(
                                report = report,
                                onClick = { onViewReport(report.effectiveReference) }
                            )
                        }
                    }
                }
            }

            // ── 7. Report by Department Grid ──────────────────────
            item {
                Text(
                    text = "REPORT BY DEPARTMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                val departments = CIVIC_DEPARTMENTS
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (i in departments.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val dept1 = departments[i]
                            DepartmentCard(
                                dept = dept1,
                                onClick = { onSelectDepartment(dept1.code) },
                                modifier = Modifier.weight(1f)
                            )
                            if (i + 1 < departments.size) {
                                val dept2 = departments[i + 1]
                                DepartmentCard(
                                    dept = dept2,
                                    onClick = { onSelectDepartment(dept2.code) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // ── 8. Floating Bottom Voice Bar ──────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(CivicTheme.colors.surface0.copy(alpha = 0.95f))
                .border(0.5.dp, CivicTheme.colors.border)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // "Describe problem in Marathi, Hindi, or English"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.surface1)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(999.dp))
                        .clickable { onStartTextCompose() }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Speak or type in Marathi, Hindi, English...",
                        fontSize = 13.sp,
                        color = CivicTheme.colors.textTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Voice Mic Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    CivicTheme.colors.primaryContainer,
                                    CivicTheme.colors.accent
                                )
                            )
                        )
                        .clickable { onStartVoiceCompose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Start Voice Report",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEmergency: Boolean = false
) {
    val cardBg = if (isEmergency) CivicTheme.colors.criticalBg else CivicTheme.colors.surface0
    val cardBorder = if (isEmergency) CivicTheme.colors.criticalBorder else CivicTheme.colors.border

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isEmergency) CivicTheme.colors.critical else CivicTheme.colors.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = CivicTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatColumn(
    number: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CivicTheme.colors.textPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = CivicTheme.colors.textTertiary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StitchActiveReportCard(
    report: ActiveReportModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = report.effectiveReference,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.accent
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SeverityBadge(severity = report.severity)
                StatusBadge(status = report.status)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = report.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = CivicTheme.colors.textPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Track progress",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.accent
            )
            Text(
                text = "View details →",
                fontSize = 11.sp,
                color = CivicTheme.colors.textTertiary
            )
        }
    }
}

@Composable
private fun DepartmentCard(
    dept: CivicDepartment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Text(
            text = dept.icon,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = dept.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = dept.description,
            fontSize = 11.sp,
            color = CivicTheme.colors.textTertiary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 15.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
