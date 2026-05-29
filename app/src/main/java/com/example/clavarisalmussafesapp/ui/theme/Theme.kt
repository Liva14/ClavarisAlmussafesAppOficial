package com.example.clavarisalmussafesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de colores en escala de grises para un diseño elegante
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)
private val DarkGray = Color(0xFF1C1C1C)
private val MediumGray = Color(0xFF757575)
private val LightGray = Color(0xFFE0E0E0)

/**
 * Esquema de colores para el modo oscuro.
 */
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

/**
 * Esquema de colores para el modo claro.
 */
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

/**
 * Tema principal de la aplicación Clavaris Almussafes.
 * Aplica los colores y la tipografía seleccionados según el modo del sistema o preferencia del usuario.
 */
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
