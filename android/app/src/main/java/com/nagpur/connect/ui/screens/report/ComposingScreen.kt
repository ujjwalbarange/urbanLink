package com.nagpur.connect.ui.screens.report

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun ComposingScreen(
    draft: IncidentDraftState,
    onUpdateText: (String) -> Unit,
    onAddPhoto: (Uri) -> Unit,
    onRemovePhoto: (Int) -> Unit,
    onSetLocation: (String, Double?, Double?) -> Unit,
    onClearCategory: () -> Unit,
    onCancel: () -> Unit,
    onReviewReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(draft.source == "voice") }

    val speechManager = remember { SpeechToTextManager(context) }
    val speechTranscript by speechManager.transcript.collectAsState()
    val speechStatus by speechManager.status.collectAsState()

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            speechManager.destroy()
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            isRecording = true
            speechManager.startListening("en-IN")
        }
    }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.take(3 - draft.photoUris.size).forEach { uri ->
            onAddPhoto(uri)
        }
    }

    // Location permission launcher
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        val latStr = "%.5f".format(loc.latitude)
                        val lngStr = "%.5f".format(loc.longitude)
                        onSetLocation("$latStr, $lngStr (GPS)", loc.latitude, loc.longitude)
                    } else {
                        onSetLocation("Nagpur, Maharashtra", 21.1458, 79.0882)
                    }
                }
            } catch (e: SecurityException) {
                onSetLocation("Nagpur, Maharashtra", 21.1458, 79.0882)
            }
        }
    }

    fun requestLocation() {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        val latStr = "%.5f".format(loc.latitude)
                        val lngStr = "%.5f".format(loc.longitude)
                        onSetLocation("$latStr, $lngStr (GPS)", loc.latitude, loc.longitude)
                    } else {
                        onSetLocation("Nagpur, Maharashtra", 21.1458, 79.0882)
                    }
                }
            } catch (e: SecurityException) {
                onSetLocation("Nagpur, Maharashtra", 21.1458, 79.0882)
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

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
            Text(
                text = if (isRecording) "Listening..." else "Describe the Issue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary
            )
            Text(
                text = "Cancel",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CivicTheme.colors.textTertiary,
                modifier = Modifier.clickable { onCancel() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Selected Department Badge ────────────────────────
        if (!draft.selectedCategory.isNullOrBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.accentMuted)
                        .border(1.dp, CivicTheme.colors.accent.copy(alpha = 0.2f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = draft.selectedCategory.replace("_", " ").uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.accent
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Change",
                    fontSize = 12.sp,
                    color = CivicTheme.colors.textTertiary,
                    modifier = Modifier.clickable { onClearCategory() }
                )
            }
        }

        // ── Voice Recording Visualizer ───────────────────────
        if (isRecording) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.9f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CivicTheme.colors.accentMuted)
                    .border(1.dp, CivicTheme.colors.accent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.accent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Recording",
                            tint = CivicTheme.colors.accent,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Speak now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.accent
                    )
                    Text(
                        text = "Nagpur Connect is listening to your voice input...",
                        fontSize = 12.sp,
                        color = CivicTheme.colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )
                    if (speechTranscript.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CivicTheme.colors.surface0)
                                .border(0.5.dp, CivicTheme.colors.border, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = speechTranscript,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CivicTheme.colors.textPrimary
                            )
                        }
                    }
                    Button(
                        onClick = {
                            speechManager.stopListening()
                            if (speechTranscript.isNotBlank()) {
                                val merged = if (draft.text.isBlank()) speechTranscript else "${draft.text.trim()} $speechTranscript"
                                onUpdateText(merged)
                            }
                            isRecording = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text("Stop Recording", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── Multiline Description Input ──────────────────────
        OutlinedTextField(
            value = draft.text,
            onValueChange = onUpdateText,
            placeholder = {
                Text(
                    text = "Describe what happened, where, and when...",
                    fontSize = 14.sp,
                    color = CivicTheme.colors.textTertiary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CivicTheme.colors.surface1,
                unfocusedContainerColor = CivicTheme.colors.surface1,
                focusedBorderColor = CivicTheme.colors.accent.copy(alpha = 0.5f),
                unfocusedBorderColor = CivicTheme.colors.border,
                focusedTextColor = CivicTheme.colors.textPrimary,
                unfocusedTextColor = CivicTheme.colors.textPrimary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Quick Action Pills (Photo, Location, Voice) ──────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Photo Action
            ActionChip(
                icon = Icons.Default.PhotoCamera,
                label = if (draft.photoUris.isNotEmpty()) "Photo (${draft.photoUris.size})" else "Photo",
                onClick = { photoPickerLauncher.launch("image/*") }
            )

            // Location Action
            ActionChip(
                icon = Icons.Default.LocationOn,
                label = if (draft.locationText.isNotBlank()) "📍 Located" else "Location",
                onClick = { requestLocation() }
            )

            // Voice Action
            ActionChip(
                icon = Icons.Default.Mic,
                label = "Voice",
                onClick = {
                    val recordAudioGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    if (recordAudioGranted) {
                        isRecording = true
                        speechManager.startListening("en-IN")
                    } else {
                        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            )
        }

        // ── Photo Thumbnails Row ─────────────────────────────
        if (draft.photoUris.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                draft.photoUris.forEachIndexed { index, uri ->
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Attached photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(2.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .clickable { onRemovePhoto(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove photo",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Review Report CTA ────────────────────────────────
        Button(
            onClick = onReviewReport,
            enabled = draft.isReadyForAnalysis,
            colors = ButtonDefaults.buttonColors(
                containerColor = CivicTheme.colors.accent,
                disabledContainerColor = CivicTheme.colors.accent.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(999.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Review Report →",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (!draft.isReadyForAnalysis && draft.text.isNotBlank()) {
            Text(
                text = "Please provide at least 10 characters for AI analysis",
                fontSize = 11.sp,
                color = CivicTheme.colors.textTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(CivicTheme.colors.surface1)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(999.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = CivicTheme.colors.textSecondary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = CivicTheme.colors.textSecondary
        )
    }
}
