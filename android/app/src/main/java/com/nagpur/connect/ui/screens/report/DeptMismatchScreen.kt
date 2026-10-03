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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.ui.screens.home.getDepartmentByCode
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun DeptMismatchScreen(
    analysis: AnalysisResultModel,
    selectedDepartmentSlug: String,
    onAcceptSuggested: (String) -> Unit,
    onOverride: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDept = getDepartmentByCode(selectedDepartmentSlug)
    val suggestedSlug = analysis.suggestedCategory ?: "general"
    val suggestedDept = getDepartmentByCode(suggestedSlug)

    val selectedName = selectedDept?.name ?: selectedDepartmentSlug.replace("_", " ").capitalize()
    val suggestedName = analysis.suggestedCategoryName ?: suggestedDept?.name ?: suggestedSlug.replace("_", " ").capitalize()

    val selectedIcon = selectedDept?.icon ?: "📋"
    val suggestedIcon = suggestedDept?.icon ?: "📋"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ──────────────────────────────────────────
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
                text = "Department Check",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Mismatch Card ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.highBorder, RoundedCornerShape(16.dp))
        ) {
            Column {
                // Top orange accent line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(CivicTheme.colors.medium, CivicTheme.colors.high)
                            )
                        )
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    // Warning Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CivicTheme.colors.highBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = CivicTheme.colors.high,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "This may be the wrong department",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = analysis.mismatchReason
                            ?: "Your report doesn't seem to match $selectedName. It looks like it belongs to $suggestedName.",
                        fontSize = 13.sp,
                        color = CivicTheme.colors.textSecondary,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Comparison Grid (You Selected vs Suggested) ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Selected (Wrong)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CivicTheme.colors.surface1)
                                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = selectedIcon, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "YOU SELECTED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CivicTheme.colors.textTertiary,
                                        letterSpacing = 0.4.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CivicTheme.colors.textSecondary
                                )
                            }
                        }

                        // Suggested (Correct)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CivicTheme.colors.accentMuted)
                                .border(1.dp, CivicTheme.colors.accent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = suggestedIcon, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "SUGGESTED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CivicTheme.colors.accent,
                                            letterSpacing = 0.4.sp
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(CivicTheme.colors.accent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Correct",
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = suggestedName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicTheme.colors.textPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Primary Action: Switch ───────────────────────
                    Button(
                        onClick = { onAcceptSuggested(suggestedSlug) },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Switch to $suggestedName →",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Secondary Action: Override ───────────────────
                    OutlinedButton(
                        onClick = onOverride,
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(
                            text = "Keep $selectedName Anyway",
                            fontSize = 13.sp,
                            color = CivicTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}
