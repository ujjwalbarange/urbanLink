package com.nagpur.connect.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Header ───────────────────────────────────────
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
                text = "Your Report Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Summary Card ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column {
                // Severity + Priority row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SeverityBadge(severity = effectiveSeverity)
                    Text(
                        text = "Priority: $effectivePriority/100",
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textTertiary
                    )
                    finalReport?.affectedPeople?.let {
                        Text(
                            text = "• $it affected",
                            fontSize = 12.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Text
                Text(
                    text = effectiveSummary,
                    fontSize = 14.sp,
                    color = CivicTheme.colors.textPrimary,
                    lineHeight = 21.sp
                )

                // Key Findings
                if (finalReport != null && finalReport.keyFindings.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "KEY FINDINGS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    finalReport.keyFindings.forEach { finding ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                color = CivicTheme.colors.accent,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = finding,
                                fontSize = 12.sp,
                                color = CivicTheme.colors.textSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // Recommended Actions (What Happens Next)
                if (finalReport != null && finalReport.recommendedActions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "WHAT HAPPENS NEXT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    finalReport.recommendedActions.forEach { action ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "→",
                                color = CivicTheme.colors.low,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = action,
                                fontSize = 12.sp,
                                color = CivicTheme.colors.textSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // Notifying Departments
                if (analysis.departments.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "NOTIFYING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        analysis.departments.forEach { d ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(CivicTheme.colors.accentMuted)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = d.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CivicTheme.colors.accent
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Emergency Flag Banner ────────────────────────────
        if (analysis.isEmergency) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CivicTheme.colors.criticalBg)
                    .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚨", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Emergency Flagged",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.critical
                        )
                        Text(
                            text = "Priority response will be triggered immediately.",
                            fontSize = 11.sp,
                            color = CivicTheme.colors.textSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Submit Report Button ─────────────────────────────
        Button(
            onClick = onSubmitReport,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = "Submit Report",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Submit",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Your report will be saved and departments will be notified",
            fontSize = 11.sp,
            color = CivicTheme.colors.textTertiary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
