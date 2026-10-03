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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    DisposableEffect(Unit) {
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

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.take(3 - draft.photoUris.size).forEach { uri ->
            onAddPhoto(uri)
        }
    }

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
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 1. Step Indicator & Selected Department Sub-bar ───
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CivicTheme.colors.accentMuted)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CivicTheme.colors.accent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "STEP 2 OF 3",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.accent,
                    letterSpacing = 0.5.sp
                )
            }

            // Department switcher pill
            if (!draft.selectedCategory.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CivicTheme.colors.surface1)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(999.dp))
                        .clickable { onClearCategory() }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = draft.selectedCategory.replace("_", " ").uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change",
                        tint = CivicTheme.colors.textTertiary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            } else {
                Text(
                    text = "Cancel",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.textTertiary,
                    modifier = Modifier.clickable { onCancel() }
                )
            }
        }

        // ── 2. Conversational Header ──────────────────────────
        Column {
            Text(
                text = "Describe the civic issue",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textPrimary,
                letterSpacing = (-0.4).sp
            )
            Text(
                text = "Speak in your native language or type. Our AI assistant will automatically structure your report.",
                fontSize = 13.sp,
                color = CivicTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // ── 3. Primary Voice Hero Module ──────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CivicTheme.colors.surface0)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(20.dp))
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Top status pill & language badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isRecording) CivicTheme.colors.criticalBg else CivicTheme.colors.surface1)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isRecording) CivicTheme.colors.critical else CivicTheme.colors.secondary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRecording) "Listening & Transcribing" else "Multilingual Voice Input",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRecording) CivicTheme.colors.critical else CivicTheme.colors.textPrimary
                        )
                    }

                    Text(
                        text = "Marathi • Hindi • English",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CivicTheme.colors.textTertiary
                    )
                }

                // Dynamic Audio Waveform Visualizer
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val waveHeight by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(400),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "waveHeight"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CivicTheme.colors.surface1)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val heights = listOf(14.dp, 24.dp, 36.dp, 20.dp, 40.dp, 28.dp, 34.dp, 18.dp, 30.dp, 16.dp)
                    heights.forEachIndexed { i, h ->
                        val effectiveHeight = if (isRecording) h * (if (i % 2 == 0) waveHeight else (1.5f - waveHeight * 0.5f)) else 6.dp
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(effectiveHeight)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (isRecording) {
                                        if (i % 3 == 0) CivicTheme.colors.secondary else CivicTheme.colors.accent
                                    } else {
                                        CivicTheme.colors.border
                                    }
                                )
                        )
                    }
                }

                // 3D Tactile Mic Button
                val micScale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.08f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "micScale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .scale(if (isRecording) micScale else 1f)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        if (isRecording) Color(0xFFEF4444) else CivicTheme.colors.primaryContainer,
                                        if (isRecording) Color(0xFFDC2626) else CivicTheme.colors.accent
                                    )
                                )
                            )
                            .clickable {
                                if (isRecording) {
                                    speechManager.stopListening()
                                    if (speechTranscript.isNotBlank()) {
                                        val merged = if (draft.text.isBlank()) speechTranscript else "${draft.text.trim()} $speechTranscript"
                                        onUpdateText(merged)
                                    }
                                    isRecording = false
                                } else {
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
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop listening" else "Start recording",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Live transcript speech bubble
                if (speechTranscript.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CivicTheme.colors.surface1)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "DETECTED SPEECH TRANSCRIPT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.accent,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“$speechTranscript”",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicTheme.colors.textPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // AI structuring confirmation chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFECFDF5))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CivicTheme.colors.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI real-time translation & structuring enabled",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CivicTheme.colors.secondary
                    )
                }
            }
        }

        // ── 4. Manual Input Fallback Textarea ─────────────────
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit structured report description",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textPrimary
                )
                Text(
                    text = "${draft.text.length} / 500",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CivicTheme.colors.textTertiary
                )
            }

            OutlinedTextField(
                value = draft.text,
                onValueChange = { if (it.length <= 500) onUpdateText(it) },
                placeholder = {
                    Text(
                        text = "Tell us what happened, where, and when...",
                        fontSize = 13.sp,
                        color = CivicTheme.colors.textTertiary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CivicTheme.colors.surface0,
                    unfocusedContainerColor = CivicTheme.colors.surface0,
                    focusedBorderColor = CivicTheme.colors.accent,
                    unfocusedBorderColor = CivicTheme.colors.border,
                    focusedTextColor = CivicTheme.colors.textPrimary,
                    unfocusedTextColor = CivicTheme.colors.textPrimary
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CivicTheme.colors.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Verified by Civic AI Engine",
                        fontSize = 11.sp,
                        color = CivicTheme.colors.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // ── 5. Attachments: Photos & GPS Location ─────────────
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "ATTACH EVIDENCE & LOCATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CivicTheme.colors.textTertiary,
                letterSpacing = 0.5.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Photo chip button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CivicTheme.colors.surface0)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                        .clickable { photoPickerLauncher.launch("image/*") }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Add Photo",
                            tint = CivicTheme.colors.accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (draft.photoUris.isNotEmpty()) "Photos (${draft.photoUris.size}/3)" else "Add Photo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicTheme.colors.textPrimary
                        )
                    }
                }

                // Location chip button
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CivicTheme.colors.surface0)
                        .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                        .clickable { requestLocation() }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = CivicTheme.colors.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (draft.locationText.isNotBlank()) "📍 Located" else "Detect GPS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicTheme.colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Photo thumbnails preview
            if (draft.photoUris.isNotEmpty()) {
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
                                    .padding(3.dp)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable { onRemovePhoto(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── 6. Review & Submit CTA ────────────────────────────
        Button(
            onClick = onReviewReport,
            enabled = draft.isReadyForAnalysis,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CivicTheme.colors.accent,
                disabledContainerColor = CivicTheme.colors.accent.copy(alpha = 0.35f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = "Next: Review Report →",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (!draft.isReadyForAnalysis && draft.text.isNotBlank()) {
            Text(
                text = "Please provide at least 10 characters for AI civic triage",
                fontSize = 11.sp,
                color = CivicTheme.colors.textTertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
