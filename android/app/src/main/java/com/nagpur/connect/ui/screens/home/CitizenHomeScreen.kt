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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Active Reports Section ───────────────────────
            if (activeReports.isNotEmpty()) {
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ACTIVE REPORTS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textTertiary,
                                letterSpacing = 0.5.sp
                            )
                            if (activeReports.size > 3) {
                                Text(
                                    text = "View all ${activeReports.size} →",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CivicTheme.colors.accent,
                                    modifier = Modifier.clickable { onViewAllReports() }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        activeReports.take(3).forEach { report ->
                            ActiveReportItemCard(
                                report = report,
                                onClick = { onViewReport(report.effectiveReference) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // ── Department Grid Header ───────────────────────
            item {
                Text(
                    text = "REPORT AN ISSUE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            // ── Department Cards Grid ────────────────────────
            item {
                // 2-column grid layout for civic departments
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

        // ── Floating Bottom Bar ──────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(CivicTheme.colors.surface0.copy(alpha = 0.95f))
                .border(0.5.dp, CivicTheme.colors.border)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // "Describe your problem..." text button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.surface1)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(999.dp))
                        .clickable { onStartTextCompose() }
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Describe your problem...",
                        fontSize = 14.sp,
                        color = CivicTheme.colors.textTertiary
                    )
                }

                // Voice mic button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.accent)
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
private fun ActiveReportItemCard(
    report: ActiveReportModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
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
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.accent
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SeverityBadge(severity = report.severity)
                StatusBadge(status = report.status)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = report.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = CivicTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Text(
            text = dept.icon,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = dept.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
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
