package com.nagpur.connect.ui.screens.track

import android.content.Intent
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nagpur.connect.data.model.TrackingIncidentResponse
import com.nagpur.connect.data.repository.IncidentRepository
import com.nagpur.connect.ui.components.SeverityBadge
import com.nagpur.connect.ui.components.StatusBadge
import com.nagpur.connect.ui.theme.CivicTheme
import kotlinx.coroutines.launch

@Composable
fun TrackIncidentScreen(
    initialReference: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { IncidentRepository(context) }

    var searchReference by remember { mutableStateOf(initialReference ?: "") }
    var isLoading by remember { mutableStateOf(!initialReference.isNullOrBlank()) }
    var trackingData by remember { mutableStateOf<TrackingIncidentResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun loadIncident(ref: String) {
        if (ref.isBlank()) return
        isLoading = true
        errorMessage = null
        scope.launch {
            repository.getIncidentByReference(ref).onSuccess { data ->
                trackingData = data
                isLoading = false
            }.onFailure { err ->
                errorMessage = err.message ?: "Report not found"
                isLoading = false
            }
        }
    }

    LaunchedEffect(initialReference) {
        if (!initialReference.isNullOrBlank()) {
            loadIncident(initialReference)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CivicTheme.colors.canvas)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 1. Top Bar & Utility Icons ────────────────────────
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
                    text = "Report Tracking",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary
                )
            }

            // Share & PDF actions
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.surface0)
                        .border(1.dp, CivicTheme.colors.border, CircleShape)
                        .clickable {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Tracking civic issue: ${searchReference.ifBlank { "NAG-2026-REPORT" }} on Urban Link")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Report"))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = CivicTheme.colors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.surface0)
                        .border(1.dp, CivicTheme.colors.border, CircleShape)
                        .clickable {
                            Toast.makeText(context, "Official PDF ticket downloaded", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "Download PDF",
                        tint = CivicTheme.colors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // ── 2. Search Input ───────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchReference,
                onValueChange = { searchReference = it },
                placeholder = { Text("Enter Reference ID (e.g. NAG-2026-0001)", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CivicTheme.colors.surface0,
                    unfocusedContainerColor = CivicTheme.colors.surface0,
                    focusedBorderColor = CivicTheme.colors.accent,
                    unfocusedBorderColor = CivicTheme.colors.border
                )
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CivicTheme.colors.accent)
                    .clickable { loadIncident(searchReference.trim()) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ── 3. Loading & Error States ─────────────────────────
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CivicTheme.colors.accent)
            }
        }

        if (!isLoading && errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.criticalBg)
                    .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "⚠️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage!!,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.critical
                    )
                }
            }
        }

        // ── 4. Loaded Ticket Header Hero Card ─────────────────
        val data = trackingData
        if (!isLoading && data?.incident != null) {
            val inc = data.incident

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CivicTheme.colors.surface0)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(20.dp))
                    .shadow(2.dp, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "MUNICIPAL GRIEVANCE ID",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textTertiary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = inc.publicReference,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textPrimary
                            )
                        }

                        StatusBadge(status = inc.status)
                    }

                    // Authority Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = CivicTheme.colors.accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nagpur Municipal Corporation",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }

                    // Meta Details Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CivicTheme.colors.surface1)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "REGISTERED", fontSize = 10.sp, color = CivicTheme.colors.textTertiary, fontWeight = FontWeight.Bold)
                            Text(
                                text = inc.createdAt.take(10),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicTheme.colors.textPrimary
                            )
                        }

                        Box(modifier = Modifier.size(1.dp, 24.dp).background(CivicTheme.colors.border))

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "SEVERITY", fontSize = 10.sp, color = CivicTheme.colors.textTertiary, fontWeight = FontWeight.Bold)
                            SeverityBadge(severity = inc.severity)
                        }
                    }
                }
            }

            // ── 5. Civic Action Lifecycle Stepper ─────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CivicTheme.colors.surface0)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(20.dp))
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                tint = CivicTheme.colors.accent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Civic Action Lifecycle",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.textPrimary
                            )
                        }
                        Text(
                            text = "Auto-refreshed",
                            fontSize = 11.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }

                    // Stepper steps
                    val stages = listOf(
                        Triple("Report Submitted", "Incident logged & reference assigned", true),
                        Triple("AI Verified & Categorized", "Auto-triaged with severity classification", true),
                        Triple("Officer Assigned", "Field crew dispatched to zone", inc.status != "DRAFT" && inc.status != "CONFIRMED"),
                        Triple("In Progress", "Repair and remediation underway", inc.status == "IN_PROGRESS" || inc.status == "RESOLVED"),
                        Triple("Resolved & Verified", "Issue fixed & citizen verified", inc.status == "RESOLVED" || inc.status == "CLOSED")
                    )

                    stages.forEachIndexed { i, step ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (step.third) Color(0xFFECFDF5) else CivicTheme.colors.surface1)
                                        .border(
                                            1.dp,
                                            if (step.third) CivicTheme.colors.secondary else CivicTheme.colors.border,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (step.third) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = CivicTheme.colors.secondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(CivicTheme.colors.textTertiary)
                                        )
                                    }
                                }
                                if (i < stages.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(28.dp)
                                            .background(if (step.third) CivicTheme.colors.secondary else CivicTheme.colors.border)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = step.first,
                                    fontSize = 13.sp,
                                    fontWeight = if (step.third) FontWeight.Bold else FontWeight.Medium,
                                    color = if (step.third) CivicTheme.colors.textPrimary else CivicTheme.colors.textTertiary
                                )
                                Text(
                                    text = step.second,
                                    fontSize = 11.sp,
                                    color = CivicTheme.colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }

            // ── 6. Description & Location Card ────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.surface0)
                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ISSUE DETAILS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    inc.title?.let { t ->
                        Text(
                            text = t,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }
                    if (!inc.citizenSummary.isNullOrBlank()) {
                        Text(
                            text = inc.citizenSummary,
                            fontSize = 12.sp,
                            color = CivicTheme.colors.textSecondary,
                            lineHeight = 16.sp
                        )
                    }
                    if (!inc.locationText.isNullOrBlank()) {
                        Text(
                            text = "📍 ${inc.locationText}",
                            fontSize = 11.sp,
                            color = CivicTheme.colors.accent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Attached Media from data.media
            if (data.media.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ATTACHED EVIDENCE (${data.media.size})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.media.forEach { mediaItem ->
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = mediaItem.storageUrl,
                                    contentDescription = mediaItem.fileName.ifBlank { "Evidence" },
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
