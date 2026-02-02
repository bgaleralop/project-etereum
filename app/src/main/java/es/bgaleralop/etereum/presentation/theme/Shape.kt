package es.bgaleralop.etereum.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(0.dp) // Para contenedores de pantalla completa
)

// Constantes de espaciado (Paddings)
object Dimensions {
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp

    val ScreenPadding = 16.dp    // Margen general de la pantalla
    val ControlSpacing = 12.dp   // Espacio entre controles (sliders, checks)
    val MinTouchTarget = 48.dp   // Para botones táctiles
    val DividerThickness = 1.dp

    val TouchTargetSize = 48.dp // Tamaño mínimo de interacción
    val StandardElevation = 2.dp // Elevación mínima para mantener sobriedad
}