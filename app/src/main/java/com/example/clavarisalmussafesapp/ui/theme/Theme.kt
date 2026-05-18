package com.example.clavarisalmussafesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Monochrome Palette
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)
private val DarkGray = Color(0xFF1C1C1C)
private val MediumGray = Color(0xFF757575)
private val LightGray = Color(0xFFE0E0E0)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    secondary = MediumGray,
    tertiary = LightGray,
    background = Black,
    surface = DarkGray,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = Black,
    onBackground = White,
    onSurface = White,
    surfaceVariant = Color(0xFF2C2C2C),
    outline = MediumGray
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = MediumGray,
    tertiary = DarkGray,
    background = White,
    surface = LightGray,
    onPrimary = White,
    onSecondary = Black,
    onTertiary = White,
    onBackground = Black,
    onSurface = Black,
    surfaceVariant = Color(0xFFEEEEEE),
    outline = MediumGray
)

@Composable
fun ClavarisAlmussafesAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
