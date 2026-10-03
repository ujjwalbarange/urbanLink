package com.nagpur.connect.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom civic design palette exposed through CivicTheme.colors
data class CivicCustomColors(
    val canvas: Color,
    val surface0: Color,
    val surface1: Color,
    val surface2: Color,
    val surface3: Color,
    val accent: Color,
    val accentHover: Color,
    val accentMuted: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textInverse: Color,
    val critical: Color,
    val criticalBg: Color,
    val criticalBorder: Color,
    val high: Color,
    val highBg: Color,
    val highBorder: Color,
    val medium: Color,
    val mediumBg: Color,
    val mediumBorder: Color,
    val low: Color,
    val lowBg: Color,
    val lowBorder: Color,
    val border: Color,
    val borderHover: Color,
    val divider: Color
)

val LightCivicColors = CivicCustomColors(
    canvas = CanvasLight,
    surface0 = Surface0Light,
    surface1 = Surface1Light,
    surface2 = Surface2Light,
    surface3 = Surface3Light,
    accent = CivicBlue,
    accentHover = CivicBlueHover,
    accentMuted = CivicBlueMuted,
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    textInverse = TextInverseLight,
    critical = CriticalRedLight,
    criticalBg = CriticalBgLight,
    criticalBorder = CriticalBorderLight,
    high = HighOrangeLight,
    highBg = HighBgLight,
    highBorder = HighBorderLight,
    medium = MediumYellowLight,
    mediumBg = MediumBgLight,
    mediumBorder = MediumBorderLight,
    low = LowGreenLight,
    lowBg = LowBgLight,
    lowBorder = LowBorderLight,
    border = BorderLight,
    borderHover = BorderHoverLight,
    divider = DividerLight
)

val DarkCivicColors = CivicCustomColors(
    canvas = CanvasDark,
    surface0 = Surface0Dark,
    surface1 = Surface1Dark,
    surface2 = Surface2Dark,
    surface3 = Surface3Dark,
    accent = CivicBlueDark,
    accentHover = CivicBlueDark,
    accentMuted = CivicBlueDarkMuted,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    textInverse = TextInverseDark,
    critical = CriticalRedDark,
    criticalBg = CriticalBgDark,
    criticalBorder = CriticalBorderDark,
    high = HighOrangeDark,
    highBg = HighBgDark,
    highBorder = HighBorderDark,
    medium = MediumYellowDark,
    mediumBg = MediumBgDark,
    mediumBorder = MediumBorderDark,
    low = LowGreenDark,
    lowBg = LowBgDark,
    lowBorder = LowBorderDark,
    border = BorderDark,
    borderHover = BorderHoverDark,
    divider = DividerDark
)

val LocalCivicColors = staticCompositionLocalOf { LightCivicColors }

object CivicTheme {
    val colors: CivicCustomColors
        @Composable
        get() = LocalCivicColors.current
}

private val LightColorScheme = lightColorScheme(
    primary = CivicBlue,
    onPrimary = Color.White,
    primaryContainer = CivicBlueMuted,
    onPrimaryContainer = CivicBlue,
    background = CanvasLight,
    onBackground = TextPrimaryLight,
    surface = Surface0Light,
    onSurface = TextPrimaryLight,
    surfaceVariant = Surface1Light,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = CivicBlueDark,
    onPrimary = CanvasDark,
    primaryContainer = CivicBlueDarkMuted,
    onPrimaryContainer = Color.White,
    background = CanvasDark,
    onBackground = TextPrimaryDark,
    surface = Surface0Dark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Surface1Dark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark
)

@Composable
fun NagpurConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val civicColors = if (darkTheme) DarkCivicColors else LightCivicColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = civicColors.surface0.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalCivicColors provides civicColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
