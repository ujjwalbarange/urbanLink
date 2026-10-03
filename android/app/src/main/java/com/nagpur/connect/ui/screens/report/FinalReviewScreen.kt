package com.nagpur.connect.ui.screens.report

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.data.model.GroqFinalReportModel
import com.nagpur.connect.ui.components.SeverityBadge
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun FinalReviewScreen(
    analysis: AnalysisResultModel,
    finalReport: GroqFinalReportModel?,
    onSubmitReport: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val effectiveSeverity = finalReport?.severity ?: analysis.severity.level
    val effectivePriority = finalReport?.priorityScore ?: analysis.priority.score
    val effectiveSummary = finalReport?.finalSummary?.ifBlank { null } ?: analysis.summary
    val primaryDept = analysis.departments.firstOrNull()?.name ?: "Municipal Corporation"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 1. Top Navigation & Step Indicator ───────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = CivicTheme.colors.textPrimary
                    )
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.surface1)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.accent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "STEP 3 OF 3: REVIEW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.accent
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CivicTheme.colors.surface1)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = CivicTheme.colors.textSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Nagpur NMC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.textSecondary
                )
            }
        }

        // ── 2. Page Introduction ──────────────────────────────
        Column {
            Text(
                text = "Review your report",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary,
                letterSpacing = (-0.4).sp
            )
            Text(
                text = "Confirm details before routing to Nagpur Municipal Corporation field crew.",
                fontSize = 13.sp,
                color = CivicTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // ── 3. AI Civic Analysis Card ─────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFDBE1FF).copy(alpha = 0.5f),
                            CivicTheme.colors.surface0,
                            CivicTheme.colors.surface1
                        )
                    )
                )
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(20.dp))
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CivicTheme.colors.accent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "AI Civic Analysis",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textPrimary
                            )
                            Text(
                                text = "Automated triage complete",
                                fontSize = 11.sp,
                                color = CivicTheme.colors.textTertiary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFECFDF5))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = CivicTheme.colors.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "98% Match",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.secondary
                        )
                    }
                }

                // AI Diagnostic Strip
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CivicTheme.colors.surface0)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Routed Division", fontSize = 12.sp, color = CivicTheme.colors.textSecondary)
                        Text(
                            text = primaryDept,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Severity Index", fontSize = 12.sp, color = CivicTheme.colors.textSecondary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SeverityBadge(severity = effectiveSeverity)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "($effectivePriority/100)",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CivicTheme.colors.textTertiary
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Estimated Response", fontSize = 12.sp, color = CivicTheme.colors.textSecondary)
                        Text(
                            text = "Under 4 hours",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.accent
                        )
                    }
                }
            }
        }

        // ── 4. Card 1: Assigned Authority ─────────────────────
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDBE1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏢", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ASSIGNED AUTHORITY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textTertiary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = primaryDept,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }
                }
            }
        }

        // ── 5. Card 2: Citizen Statement ──────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = CivicTheme.colors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CITIZEN SUMMARY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.accent,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = effectiveSummary,
                    fontSize = 13.sp,
                    color = CivicTheme.colors.textPrimary,
                    lineHeight = 18.sp
                )
            }
        }

        // ── 6. Card 3: Location Card ──────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF6CF8BB).copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = CivicTheme.colors.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "GEO-LOCATION CONTEXT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Nagpur Municipal Jurisdiction",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textPrimary
                    )
                    Text(
                        text = "Ward 14 (Dharampeth) • Automated Zone Routing",
                        fontSize = 11.sp,
                        color = CivicTheme.colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── 7. Submit Report CTA ──────────────────────────────
        Button(
            onClick = onSubmitReport,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CivicTheme.colors.accent
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        ) {
            Text(
                text = "Submit Report to NMC",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
