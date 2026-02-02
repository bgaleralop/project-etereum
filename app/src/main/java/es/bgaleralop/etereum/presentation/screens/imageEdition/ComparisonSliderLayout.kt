package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

/**
 * Componente para superponer ambas imagenes.
 *
 * @param original The original Bitmap
 * @param modified The modified Bitmap
 *
 */
@Composable
fun ComparisonSliderLayout(
    original: Bitmap?,
    modified: Bitmap?,
    modifier: Modifier = Modifier
) {
    val TAG = "ETEREUM ComparisonSliderLayout :"

    var sliderPosition by rememberSaveable { mutableFloatStateOf(0.5f) }
    val originalBitmap: ImageBitmap? = remember(original) { original?.asImageBitmap() }
    val modifiedBitmap: ImageBitmap? = remember(modified) { modified?.asImageBitmap() }
    Log.i(TAG, "Iniciado...")

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(Dimensions.ScreenPadding)) {
        // IMAGEN ORIGINAL (FONDO)
        originalBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = stringResource(R.string.original_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        // IMAGEN MODIFICADA (CAPA SUPERIOR CON RECORTE DINÁMICO)
        modifiedBitmap?.let { modified ->
            Image(
                bitmap = modified,
                contentDescription = stringResource(R.string.modified_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer() {
                        // Corta la imagen según la posición del slider
                        clip = true
                        shape = object : Shape {
                            override fun createOutline(
                                size: Size,
                                layoutDirection: LayoutDirection,
                                density: Density
                            ): Outline {
                                return Outline.Rectangle(
                                    Rect(
                                        0f,
                                        0f,
                                        size.width * sliderPosition,
                                        size.height
                                    )
                                )
                            }
                        }
                    }
            )
        }

        // CONTROL DESLIZANTE FISICO.
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(thumbColor = TacticalAmber, activeTrackColor = TacticalAmber),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Dimensions.PaddingMedium)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComparisonSliderLayoutPreview(){
    EtereumTheme {
        Scaffold { innerPadding ->
            ComparisonSliderLayout(
                original = null,
                modified = null,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}