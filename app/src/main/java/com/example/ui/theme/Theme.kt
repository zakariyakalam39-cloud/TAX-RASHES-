package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TaxBluePrimary,
    onPrimary = Color.White,
    primaryContainer = TaxPanelElevated,
    onPrimaryContainer = TaxCyanAccent,
    secondary = TaxCyanAccent,
    onSecondary = Color(0xFF001733),
    secondaryContainer = Color(0xFF132B4F),
    onSecondaryContainer = Color(0xFFCBE6FF),
    tertiary = TaxGreenSuccess,
    onTertiary = Color(0xFF00150E),
    tertiaryContainer = Color(0xFF0A3320),
    onTertiaryContainer = Color(0xFFA5F3CF),
    background = TaxBgDark,
    onBackground = TaxTextPrimary,
    surface = TaxPanelDark,
    onSurface = TaxTextPrimary,
    surfaceVariant = TaxPanelElevated,
    onSurfaceVariant = TaxTextSecondary,
    surfaceContainer = TaxPanelSurface,
    surfaceContainerHigh = TaxPanelElevated,
    outline = TaxBorder,
    outlineVariant = TaxBorderSubtle,
    error = TaxRedLive,
    onError = Color.White
)

@Composable
fun TaxRashesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
