package com.nagpur.connect.ui.screens.report

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.data.model.CreatedIncidentModel
import com.nagpur.connect.ui.components.SeverityBadge
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun ReportSuccessScreen(
    incident: CreatedIncidentModel,
    onViewReport: (String) -> Unit,
    onReportAnother: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // ── Big Green Checkmark ──────────────────────────────
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(CivicTheme.colors.lowBg)
                .border(2.dp, CivicTheme.colors.lowBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = CivicTheme.colors.low,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Report Submitted",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your report has been created and departments have been notified.",
            fontSize = 13.sp,
            color = CivicTheme.colors.textTertiary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Tracking ID Card ─────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.accentMuted)
                .border(2.dp, CivicTheme.colors.accent.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                .clickable {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Tracking Reference", incident.publicReference)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Tracking ID copied to clipboard", Toast.LENGTH_SHORT).show()
                }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "YOUR TRACKING ID",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = incident.publicReference,
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = CivicTheme.colors.accent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = CivicTheme.colors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap to copy • Save this ID to track your report status",
                    fontSize = 11.sp,
                    color = CivicTheme.colors.textTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Summary Card ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface1)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = incident.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SeverityBadge(severity = incident.severity)
                    if (incident.isEmergency) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(CivicTheme.colors.criticalBg)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🚨 Emergency",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.critical
                            )
                        }
                    }
                }
            }
        }

        // ── Notifying Departments ────────────────────────────
        if (incident.departments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.surface1)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "DEPARTMENTS BEING NOTIFIED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    incident.departments.forEach { dept ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CivicTheme.colors.accent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dept.name,
                                fontSize = 13.sp,
                                color = CivicTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Action Buttons ───────────────────────────────────
        Button(
            onClick = { onViewReport(incident.publicReference) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text("Track Report Status →", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onReportAnother,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text("Report Another Issue", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onGoHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Back to Dashboard",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CivicTheme.colors.textSecondary
            )
        }
    }
}
