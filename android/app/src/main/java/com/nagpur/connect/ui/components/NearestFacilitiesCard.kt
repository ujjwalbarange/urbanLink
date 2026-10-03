package com.nagpur.connect.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagpur.connect.ui.theme.CivicTheme

data class CivicFacility(
    val name: String,
    val department: String,
    val address: String,
    val distanceKm: Double,
    val distanceLabel: String,
    val latitude: Double,
    val longitude: Double
)

val NAGPUR_SAMPLE_FACILITIES = listOf(
    CivicFacility(
        name = "Sitabuldi Police Station",
        department = "Police Department",
        address = "Sitabuldi Main Rd, Sitabuldi, Nagpur",
        distanceKm = 0.8,
        distanceLabel = "Very Nearby",
        latitude = 21.1458,
        longitude = 79.0882
    ),
    CivicFacility(
        name = "NMC Fire Station Civil Lines",
        department = "Fire Brigade",
        address = "Civil Lines, Nagpur, Maharashtra 440001",
        distanceKm = 1.6,
        distanceLabel = "Nearby",
        latitude = 21.1550,
        longitude = 79.0760
    ),
    CivicFacility(
        name = "Government Medical College & Hospital (GMCH)",
        department = "Health Department",
        address = "Medical Square, Hanuman Nagar, Nagpur",
        distanceKm = 2.4,
        distanceLabel = "Moderate",
        latitude = 21.1270,
        longitude = 79.1000
    )
)

@Composable
fun NearestFacilitiesCard(
    facilities: List<CivicFacility> = NAGPUR_SAMPLE_FACILITIES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CivicTheme.colors.surface0)
            .border(1.dp, CivicTheme.colors.border, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "NEAREST CIVIC FACILITIES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CivicTheme.colors.textTertiary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        facilities.forEachIndexed { index, facility ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (index == 0) CivicTheme.colors.surface1 else CivicTheme.colors.surface0)
                    .border(
                        1.dp,
                        if (index == 0) CivicTheme.colors.accent.copy(alpha = 0.2f) else CivicTheme.colors.border,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = facility.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicTheme.colors.textPrimary
                        )
                        Text(
                            text = facility.address,
                            fontSize = 11.sp,
                            color = CivicTheme.colors.textTertiary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(CivicTheme.colors.lowBg)
                                .border(1.dp, CivicTheme.colors.lowBorder, RoundedCornerShape(999.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${facility.distanceKm} km • ${facility.distanceLabel}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicTheme.colors.low
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val mapUri = Uri.parse("geo:0,0?q=${facility.latitude},${facility.longitude}(${facility.name})")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                            context.startActivity(mapIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicTheme.colors.accent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = "Directions",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            if (index < facilities.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
