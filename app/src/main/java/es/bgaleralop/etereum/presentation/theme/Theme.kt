package es.bgaleralop.etereum.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NatoGreen,
    onPrimary = TextHighEmphasis,
    secondary = TacticalAmberDark,
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
    val colorScheme = DarkColorScheme

    val view = LocalView.current

    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}