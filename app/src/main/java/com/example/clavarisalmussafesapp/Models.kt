package com.example.clavarisalmussafesapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Clase base para definir las rutas de navegación.
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector)

/**
 * Modelo de datos para un evento del calendario.
 */
data class CalendarEvent(
    val date: String, // Formato esperado: YYYY-MM-DD
    val title: String,
    val time: String,
    val type: EventType
)

/**
 * Modelo de datos para una noticia.
 */
data class NewsPost(
    val title: String,
    val description: String,
    val imageResName: String? = null,
    val imageUrl: String? = null
)

/**
 * Modelo de datos para una rifa o lotería.
 */
data class LotteryPost(
    val title: String,
    val description: String,
    val imageResName: String? = null,
    val imageUrl: String? = null
)

/**
 * Modelo de datos para un patrocinador.
 */
data class Sponsor(
    val name: String,
    val imageUrl: String? = null,
    val imageResName: String? = null
)

/**
 * Tipos de eventos con sus respectivos colores para el calendario.
 */
enum class EventType(val color: Color, val label: String) {
    MERCADO(Color(0xFF2196F3), "Evento Mercado"),
    DISCOMOVIL(Color(0xFFE91E63), "Evento Discomovil"),
    PASACARRER(Color(0xFF9C27B0), "Evento Pasacarrer"),
    EVENT(Color(0xFF4CAF50), "Evento Event"),
    FIESTA(Color(0xFFFFEB3B), "Evento Fiesta")
}
