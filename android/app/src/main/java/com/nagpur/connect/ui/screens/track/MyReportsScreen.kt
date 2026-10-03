package com.nagpur.connect.ui.screens.track

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun MyReportsScreen(
    reports: List<ActiveReportModel>,
    onSelectReport: (String) -> Unit,
    onReportNew: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("All", "Active", "Resolved")

    val filteredReports = when (selectedTabIndex) {
        1 -> reports.filter { it.status != "RESOLVED" && it.status != "CLOSED" }
        2 -> reports.filter { it.status == "RESOLVED" || it.status == "CLOSED" }
        else -> reports
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
    ) {
        // ── Top Header ───────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicTheme.colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "My Grievances",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        // ── Tabs Row ─────────────────────────────────────────
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = CivicTheme.colors.surface0,
            contentColor = CivicTheme.colors.accent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = CivicTheme.colors.accent
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // ── Reports List or Empty State ──────────────────────
        if (filteredReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CivicTheme.colors.surface2),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Assignment,
                            contentDescription = "Empty",
                            tint = CivicTheme.colors.textTertiary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Grievances Found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You haven't reported any civic issues in this category yet.",
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textTertiary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onReportNew,
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text("Report an Issue", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredReports, key = { it.effectiveReference }) { report ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CivicTheme.colors.surface0)
                            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                            .clickable { onSelectReport(report.effectiveReference) }
                            .padding(16.dp)
                    ) {
                        Column {
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
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = report.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicTheme.colors.textPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (report.effectiveCreatedAt.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = report.effectiveCreatedAt.take(10),
                                    fontSize = 11.sp,
                                    color = CivicTheme.colors.textTertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
