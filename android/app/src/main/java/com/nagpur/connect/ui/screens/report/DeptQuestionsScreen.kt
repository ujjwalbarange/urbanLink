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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.ui.components.SeverityBadge
import com.nagpur.connect.ui.screens.home.getDepartmentByCode
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun DeptQuestionsScreen(
    analysis: AnalysisResultModel,
    onSubmitAnswers: (Map<String, String>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = analysis.deptQuestions
    val deptInfo = getDepartmentByCode(analysis.mainCategory)

    val answers = remember { mutableStateMapOf<String, String>() }

    val requiredAnswered = questions
        .filter { it.required }
        .all { q -> !answers[q.id].isNullOrBlank() }

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
                text = "A Few More Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Analysis Summary Card ────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = deptInfo?.icon ?: "📋", fontSize = 18.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(CivicTheme.colors.accentMuted)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = analysis.mainCategoryName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.accent
                        )
                    }
                    SeverityBadge(severity = analysis.severity.level)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = analysis.summary,
                    fontSize = 13.sp,
                    color = CivicTheme.colors.textPrimary,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HELP US RESPOND FASTER",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textTertiary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ── Dynamic Questions ────────────────────────────────
        questions.forEachIndexed { index, question ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.surface0)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(CivicTheme.colors.accentMuted),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.accent
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = question.question + if (question.required) " *" else "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicTheme.colors.textPrimary,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Chip selection (single select)
                    if (question.type == "chip" && !question.options.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            question.options.forEach { opt ->
                                val isSelected = answers[question.id] == opt
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(if (isSelected) CivicTheme.colors.accent else CivicTheme.colors.surface1)
                                        .border(
                                            1.dp,
                                            if (isSelected) CivicTheme.colors.accent else CivicTheme.colors.border,
                                            RoundedCornerShape(999.dp)
                                        )
                                        .clickable { answers[question.id] = opt }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = opt,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else CivicTheme.colors.textSecondary
                                    )
                                }
                            }
                        }
                    }

                    // Multi-chip selection
                    if (question.type == "multi_chip" && !question.options.isNullOrEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            question.options.forEach { opt ->
                                val selectedList = answers[question.id]?.split(", ") ?: emptyList()
                                val isSelected = selectedList.contains(opt)

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(if (isSelected) CivicTheme.colors.accent else CivicTheme.colors.surface1)
                                        .border(
                                            1.dp,
                                            if (isSelected) CivicTheme.colors.accent else CivicTheme.colors.border,
                                            RoundedCornerShape(999.dp)
                                        )
                                        .clickable {
                                            val updated = if (isSelected) {
                                                selectedList - opt
                                            } else {
                                                selectedList + opt
                                            }
                                            answers[question.id] = updated.joinToString(", ")
                                        }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        Text(
                                            text = opt,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSelected) Color.White else CivicTheme.colors.textSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Yes/No/Unsure
                    if (question.type == "yesno") {
                        val options = question.options ?: listOf("Yes", "No", "Unsure")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            options.forEach { opt ->
                                val isSelected = answers[question.id] == opt
                                val (bg, textCol) = when (opt) {
                                    "Yes" -> if (isSelected) CivicTheme.colors.low to Color.White else CivicTheme.colors.surface1 to CivicTheme.colors.textSecondary
                                    "No" -> if (isSelected) CivicTheme.colors.critical to Color.White else CivicTheme.colors.surface1 to CivicTheme.colors.textSecondary
                                    else -> if (isSelected) CivicTheme.colors.accent to Color.White else CivicTheme.colors.surface1 to CivicTheme.colors.textSecondary
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bg)
                                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(10.dp))
                                        .clickable { answers[question.id] = opt },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = opt,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textCol
                                    )
                                }
                            }
                        }
                    }

                    // Text Input
                    if (question.type == "text") {
                        OutlinedTextField(
                            value = answers[question.id] ?: "",
                            onValueChange = { answers[question.id] = it },
                            placeholder = { Text(question.placeholder ?: "Type here...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CivicTheme.colors.surface1,
                                unfocusedContainerColor = CivicTheme.colors.surface1,
                                focusedBorderColor = CivicTheme.colors.accent,
                                unfocusedBorderColor = CivicTheme.colors.border
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Submit Answers CTA ───────────────────────────────
        Button(
            onClick = { onSubmitAnswers(answers.toMap()) },
            enabled = requiredAnswered,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = "Continue to Final Review →",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
