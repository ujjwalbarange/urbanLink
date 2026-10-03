package com.nagpur.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.TimelineEventModel
import com.nagpur.connect.ui.theme.CivicTheme

enum class TrackingStage(val title: String, val description: String) {
    SUBMITTED("Report Submitted", "Incident captured and assigned reference"),
    ROUTED("Routed to Department", "AI dispatched ticket to responsible civic agency"),
    IN_PROGRESS("In Progress / Crew Dispatched", "Field workers deployed to address problem"),
    RESOLVED("Resolved & Verified", "Fix completed and verified")
}

@Composable
fun TrackingTimelineView(
    currentStatus: String,
    timelineEvents: List<TimelineEventModel>,
    modifier: Modifier = Modifier
) {
    val normStatus = currentStatus.uppercase()

    val currentStageIndex = when (normStatus) {
        "CONFIRMED" -> 0
        "ROUTED" -> 1
        "ASSIGNED", "IN_PROGRESS", "PENDING_VERIFICATION" -> 2
        "RESOLVED", "CLOSED" -> 3
        else -> 0
    }

    val stages = TrackingStage.values()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "PROGRESS TIMELINE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textTertiary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        stages.forEachIndexed { index, stage ->
            val isCompleted = index < currentStageIndex
            val isCurrent = index == currentStageIndex
            val isPending = index > currentStageIndex

            Row(modifier = Modifier.fillMaxWidth()) {
                // Stepper Line & Circle Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    val circleBg = when {
                        isCompleted -> CivicTheme.colors.low
                        isCurrent -> CivicTheme.colors.accent
                        else -> CivicTheme.colors.surface2
                    }
                    val circleBorder = when {
                        isCompleted -> CivicTheme.colors.low
                        isCurrent -> CivicTheme.colors.accent
                        else -> CivicTheme.colors.border
                    }

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(circleBg)
                            .border(1.dp, circleBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Color.White else CivicTheme.colors.textTertiary
                            )
                        }
                    }

                    if (index < stages.size - 1) {
                        val lineColor = if (isCompleted) CivicTheme.colors.low else CivicTheme.colors.border
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(44.dp)
                                .background(lineColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Stage Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index < stages.size - 1) 20.dp else 4.dp)
                ) {
                    val textColor = when {
                        isCurrent -> CivicTheme.colors.accent
                        isCompleted -> CivicTheme.colors.textPrimary
                        else -> CivicTheme.colors.textTertiary
                    }

                    Text(
                        text = stage.title,
                        fontSize = 14.sp,
                        fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                    Text(
                        text = stage.description,
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    // Matching timeline timestamp if available
                    val matchingEvent = timelineEvents.find { it.status.equals(stage.name, ignoreCase = true) }
                    if (matchingEvent != null) {
                        Text(
                            text = matchingEvent.timestamp,
                            fontSize = 10.sp,
                            color = CivicTheme.colors.textTertiary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
