package com.nagpur.connect.ui.screens.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nagpur.connect.ui.theme.CivicTheme

@Composable
fun DraftPreviewScreen(
    draft: IncidentDraftState,
    onEditText: () -> Unit,
    onRecordAgain: () -> Unit,
    onAddPhoto: (Uri) -> Unit,
    onRemovePhoto: (Int) -> Unit,
    onEditLocation: () -> Unit,
    onAnalyze: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.take(3 - draft.photoUris.size).forEach { uri ->
            onAddPhoto(uri)
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Report",
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

        Spacer(modifier = Modifier.height(16.dp))

        // ── Description Quotation Card ───────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface1)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "“${draft.text}”",
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = CivicTheme.colors.textPrimary,
                        lineHeight = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CivicTheme.colors.surface2)
                            .clickable { onEditText() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Text",
                            tint = CivicTheme.colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                if (draft.source == "voice") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice",
                            tint = CivicTheme.colors.textTertiary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Transcribed from voice",
                            fontSize = 11.sp,
                            color = CivicTheme.colors.textTertiary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Attachments Section ──────────────────────────────
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
                    text = "ATTACHMENTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicTheme.colors.textTertiary,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    draft.photoUris.forEachIndexed { index, uri ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Photo ${index + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(20.dp)
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

                    if (draft.photoUris.size < 3) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(12.dp))
                                .clickable { photoPickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Photo",
                                    tint = CivicTheme.colors.textTertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Add",
                                    fontSize = 10.sp,
                                    color = CivicTheme.colors.textTertiary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Location Section ─────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CivicTheme.colors.surface1)
                .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LOCATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicTheme.colors.textTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (draft.locationText.isNotBlank()) "📍 ${draft.locationText}" else "No location specified",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (draft.locationText.isNotBlank()) CivicTheme.colors.textPrimary else CivicTheme.colors.textTertiary
                    )
                }
                Text(
                    text = if (draft.locationText.isNotBlank()) "Change" else "Add location",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CivicTheme.colors.accent,
                    modifier = Modifier.clickable { onEditLocation() }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Actions ──────────────────────────────────────────
        if (draft.source == "voice") {
            OutlinedButton(
                onClick = onRecordAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CivicTheme.colors.textPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Record Again",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Record Again", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = onAnalyze,
            enabled = draft.isReadyForAnalysis,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
            shape = RoundedCornerShape(999.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Analyze",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Analyze with AI",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
