package com.nagpur.connect.ui.screens.track

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("All", "Active", "In Progress", "Resolved")

    val searchedReports = if (searchQuery.isBlank()) {
        reports
    } else {
        reports.filter {
            it.effectiveReference.contains(searchQuery, ignoreCase = true) ||
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    it.effectiveDepartment.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredReports = when (selectedTabIndex) {
        1 -> searchedReports.filter { it.status != "RESOLVED" && it.status != "CLOSED" }
        2 -> searchedReports.filter { it.status == "IN_PROGRESS" || it.status == "ROUTED" }
        3 -> searchedReports.filter { it.status == "RESOLVED" || it.status == "CLOSED" }
        else -> searchedReports
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
    ) {
        // ── 1. Top Header ─────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
            Column {
                Text(
                    text = "My Reports",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Track and inspect civic resolutions",
                    fontSize = 12.sp,
                    color = CivicTheme.colors.textSecondary
                )
            }
        }

        // ── 2. Search Bar ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search by report ID, street, or issue...",
                        fontSize = 13.sp,
                        color = CivicTheme.colors.textTertiary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CivicTheme.colors.textTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = CivicTheme.colors.textTertiary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CivicTheme.colors.surface0,
                    unfocusedContainerColor = CivicTheme.colors.surface0,
                    focusedBorderColor = CivicTheme.colors.accent,
                    unfocusedBorderColor = CivicTheme.colors.border,
                    focusedTextColor = CivicTheme.colors.textPrimary,
                    unfocusedTextColor = CivicTheme.colors.textPrimary
                )
            )
        }

        // ── 3. Horizontal Filter Tabs ─────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabTitles.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                val count = when (index) {
                    0 -> reports.size
                    1 -> reports.count { it.status != "RESOLVED" && it.status != "CLOSED" }
                    2 -> reports.count { it.status == "IN_PROGRESS" || it.status == "ROUTED" }
                    3 -> reports.count { it.status == "RESOLVED" || it.status == "CLOSED" }
                    else -> 0
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (isSelected) CivicTheme.colors.textPrimary else CivicTheme.colors.surface1)
                        .border(
                            0.5.dp,
                            if (isSelected) CivicTheme.colors.textPrimary else CivicTheme.colors.border,
                            RoundedCornerShape(999.dp)
                        )
                        .clickable { selectedTabIndex = index }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$title ($count)",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else CivicTheme.colors.textSecondary
                    )
                }
            }
        }

        // ── 4. Reports List or Empty State ────────────────────
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
                            .size(60.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(CivicTheme.colors.surface1),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Assignment,
                            contentDescription = "Empty",
                            tint = CivicTheme.colors.textTertiary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Reports Found",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "No civic issues match your current filter or search query.",
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textTertiary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onReportNew,
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Text("Report an Issue", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredReports, key = { it.effectiveReference }) { report ->
                    StitchReportHistoryCard(
                        report = report,
                        onClick = { onSelectReport(report.effectiveReference) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StitchReportHistoryCard(
    report: ActiveReportModel,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header: ID + Time + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = report.effectiveReference,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(CivicTheme.colors.textTertiary))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (report.effectiveCreatedAt.isNotBlank()) report.effectiveCreatedAt.take(10) else "Recent",
                        fontSize = 11.sp,
                        color = CivicTheme.colors.textSecondary
                    )
                }

                StatusBadge(status = report.status)
            }

            // Department Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFDBE1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏛️", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = report.effectiveDepartment,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary
                )
            }

            // Report Title / Summary
            Text(
                text = report.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = CivicTheme.colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            // Severity & Officer status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CivicTheme.colors.surface1)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Priority Level:",
                    fontSize = 11.sp,
                    color = CivicTheme.colors.textTertiary
                )
                SeverityBadge(severity = report.severity)
            }

            // Track Progress CTA Button
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CivicTheme.colors.accent
                )
            ) {
                Text(
                    text = "Track Progress",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
