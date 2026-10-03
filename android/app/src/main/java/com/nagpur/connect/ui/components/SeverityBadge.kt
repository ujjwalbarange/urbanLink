package com.nagpur.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun SeverityBadge(
    severity: String?,
    modifier: Modifier = Modifier
) {
    val sev = severity?.uppercase() ?: "MEDIUM"
    val (bgColor, textColor, borderColor) = when (sev) {
        "CRITICAL" -> Triple(
            CivicTheme.colors.criticalBg,
            CivicTheme.colors.critical,
            CivicTheme.colors.criticalBorder
        )
        "HIGH" -> Triple(
            CivicTheme.colors.highBg,
            CivicTheme.colors.high,
            CivicTheme.colors.highBorder
        )
        "MEDIUM" -> Triple(
            CivicTheme.colors.mediumBg,
            CivicTheme.colors.medium,
            CivicTheme.colors.mediumBorder
        )
        else -> Triple(
            CivicTheme.colors.lowBg,
            CivicTheme.colors.low,
            CivicTheme.colors.lowBorder
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = sev,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val normStatus = status.uppercase()
    val (bgColor, textColor, borderColor) = when (normStatus) {
        "RESOLVED", "CLOSED" -> Triple(
            CivicTheme.colors.lowBg,
            CivicTheme.colors.low,
            CivicTheme.colors.lowBorder
        )
        "IN_PROGRESS", "PENDING_VERIFICATION" -> Triple(
            CivicTheme.colors.mediumBg,
            CivicTheme.colors.medium,
            CivicTheme.colors.mediumBorder
        )
        else -> Triple(
            CivicTheme.colors.accentMuted,
            CivicTheme.colors.accent,
            CivicTheme.colors.accent.copy(alpha = 0.2f)
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = status.replace("_", " "),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
