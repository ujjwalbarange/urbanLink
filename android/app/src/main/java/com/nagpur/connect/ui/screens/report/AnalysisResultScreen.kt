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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun AnalysisResultScreen(
    analysis: AnalysisResultModel,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sev = analysis.severity
    val (sevBg, sevText, sevBorder, sevBar, sevIcon) = when (sev.level.lowercase()) {
        "critical" -> Quintuple(
            CivicTheme.colors.criticalBg,
            CivicTheme.colors.critical,
            CivicTheme.colors.criticalBorder,
            CivicTheme.colors.critical,
            "🔴"
        )
        "high" -> Quintuple(
            CivicTheme.colors.highBg,
            CivicTheme.colors.high,
            CivicTheme.colors.highBorder,
            CivicTheme.colors.high,
            "🟠"
        )
        "medium" -> Quintuple(
            CivicTheme.colors.mediumBg,
            CivicTheme.colors.medium,
            CivicTheme.colors.mediumBorder,
            CivicTheme.colors.medium,
            "🟡"
        )
        else -> Quintuple(
            CivicTheme.colors.lowBg,
            CivicTheme.colors.low,
            CivicTheme.colors.lowBorder,
            CivicTheme.colors.low,
            "🟢"
        )
    }

    val hasQuestions = analysis.deptQuestions.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Header ───────────────────────────────────────
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
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AI Analysis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary
                )
            }

            // Confidence Indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.low)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${(analysis.confidence.overall * 100).toInt()}% confident",
                    fontSize = 12.sp,
                    color = CivicTheme.colors.textTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Category & Incident Badges ───────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CivicTheme.colors.accentMuted)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = analysis.mainCategoryName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.accent
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CivicTheme.colors.surface2)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = analysis.incidentType.replace("_", " "),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = CivicTheme.colors.textSecondary
                )
            }

            if (analysis.isEmergency) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.criticalBg)
                        .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🚨 Emergency",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.critical
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Severity Card ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(sevBg)
                .border(1.dp, sevBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(text = sevIcon, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${sev.level.capitalize()} Severity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = sevText
                        )
                        Text(
                            text = "${sev.score}/100",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textTertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Score bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = (sev.score.coerceIn(5, 100) / 100f))
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(sevBar)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = sev.reason,
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── AI Understanding Summary Card ────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "AI UNDERSTANDING",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = analysis.summary,
                    fontSize = 14.sp,
                    color = CivicTheme.colors.textPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Departments to be Notified Card ──────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "DEPARTMENTS TO BE NOTIFIED (${analysis.departments.size})",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                analysis.departments.forEach { dept ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CivicTheme.colors.accent)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = dept.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicTheme.colors.textPrimary
                            )
                            Text(
                                text = dept.reason,
                                fontSize = 11.sp,
                                color = CivicTheme.colors.textTertiary,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Progress Stepper Indicator ───────────────────────
        if (hasQuestions) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Analysis", fontSize = 11.sp, color = CivicTheme.colors.textTertiary)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(1.dp)
                        .background(CivicTheme.colors.border)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.accentMuted)
                            .border(1.dp, CivicTheme.colors.accent.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "2",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.accent
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${analysis.deptQuestions.size} quick questions",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CivicTheme.colors.accent
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(1.dp)
                        .background(CivicTheme.colors.border)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.surface2)
                            .border(1.dp, CivicTheme.colors.border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "3",
                            fontSize = 10.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit", fontSize = 11.sp, color = CivicTheme.colors.textTertiary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Primary Action Button ────────────────────────────
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = if (hasQuestions) {
                    "Answer ${analysis.deptQuestions.size} Quick Questions"
                } else {
                    "Confirm & Submit Report"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Continue",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
