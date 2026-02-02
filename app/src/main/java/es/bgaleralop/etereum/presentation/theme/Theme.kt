package es.bgaleralop.etereum.presentation.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NatoGreen,
    onPrimary = TextHighEmphasis,
    secondary = TacticalAmber,
    onSecondary = DeepBlack,
    background = DeepBlack,
    surface = SurfaceGrey,
    onSurface = TextHighEmphasis,
    error = ErrorRed,
    outline = NatoGreenLight
)

private val LightColorScheme = lightColorScheme(
    primary = NatoGreen,
    onPrimary = Color.White,
    secondary = TacticalAmberDark,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onSurface = DeepBlack
)
@Composable
fun EtereumTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}