package com.nagpur.connect.ui.screens.track

import android.app.Application
import kotlinx.coroutines.launch
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.nagpur.connect.ui.components.TrackingTimelineView
import com.nagpur.connect.ui.theme.CivicTheme

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

    val scope = androidx.compose.runtime.rememberCoroutineScope()

    fun loadIncident(ref: String) {
        if (ref.isBlank()) return
        isLoading = true
        errorMessage = null
        scope.launch {
            repository.getIncidentByReference(ref).onSuccess { data ->
                trackingData = data
                isLoading = false
            }.onFailure { err ->
                errorMessage = err.message ?: "Incident not found"
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
                text = "Track Grievance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Search Input ─────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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
            Spacer(modifier = Modifier.width(8.dp))
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
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Loading State ────────────────────────────────────
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CivicTheme.colors.accent)
            }
        }

        // ── Error State ──────────────────────────────────────
        if (!isLoading && errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.criticalBg)
                    .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "⚠️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.critical
                    )
                }
            }
        }

        // ── Incident Loaded Content ──────────────────────────
        val data = trackingData
        if (!isLoading && data?.incident != null) {
            val inc = data.incident

            // Status Card
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inc.publicReference,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.accent
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SeverityBadge(severity = inc.severity)
                            StatusBadge(status = inc.status)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = inc.title ?: "Civic Report",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.textPrimary
                    )

                    inc.citizenSummary?.let { summary ->
                        Text(
                            text = summary,
                            fontSize = 13.sp,
                            color = CivicTheme.colors.textSecondary,
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = CivicTheme.colors.border, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = inc.locationText?.let { "📍 $it" } ?: "Location not specified",
                            fontSize = 12.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                        Text(
                            text = inc.createdAt.take(10),
                            fontSize = 12.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4-Stage Stepper Timeline
            TrackingTimelineView(
                currentStatus = inc.status,
                timelineEvents = data.timeline
            )

            // Departments Assigned
            if (data.departments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
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
                            text = "ASSIGNED DEPARTMENTS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textTertiary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        data.departments.forEach { dept ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dept.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CivicTheme.colors.textPrimary
                                )
                                StatusBadge(status = dept.status)
                            }
                        }
                    }
                }
            }

            // Attached Media
            if (data.media.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
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
                            text = "ATTACHED EVIDENCE (${data.media.size})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicTheme.colors.textTertiary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            data.media.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(10.dp))
                                ) {
                                    AsyncImage(
                                        model = item.storageUrl,
                                        contentDescription = item.fileName,
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
}
