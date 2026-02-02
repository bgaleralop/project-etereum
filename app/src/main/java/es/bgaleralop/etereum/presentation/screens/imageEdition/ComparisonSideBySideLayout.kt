package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.ErrorRed
import es.bgaleralop.etereum.presentation.theme.EtereumTheme

@Composable
fun ComparisonSideBySideLayout(
    original: Bitmap?,
    modified: Bitmap?,
    modifier: Modifier = Modifier
) {
    val TAG = "ETEREUM ComparisonSideBySideLayout :"

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxSize().padding(Dimensions.ScreenPadding)
    ) {
        val originalBitmap: ImageBitmap? = remember(original) { original?.asImageBitmap() }
        val modifiedBitmap: ImageBitmap? = remember(modified) { modified?.asImageBitmap() }

        // IMAGEN ORIGINAL IZQUIERDA
        Card {
            if(originalBitmap != null) {
                Log.d(TAG, "Imagen original cargada")
                Image(
                    bitmap = originalBitmap,
                    contentDescription = "Imagen Original",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().weight(0.5f)
                )
            } else {
                Log.w(TAG, "No hay imagen original cargada")
                Text("No hay imagen cargada", style = MaterialTheme.typography.labelLarge.copy(color = ErrorRed))
            }
        }

        // IMAGEN MODIFICADA DERECHA
        Card {
            if(modifiedBitmap != null) {
                Log.d(TAG, "Imagen modificada cargada")
                Image(
                    bitmap = modifiedBitmap,
                    contentDescription = "Imagen Original",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().weight(0.5f)
                )
            } else {
                Log.w(TAG, "No hay imagen modificada cargada")
                Text("No hay imagen cargada", style = MaterialTheme.typography.labelLarge.copy(color = ErrorRed))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComparisonSideBySideLayoutPreview(){
    EtereumTheme {
        Scaffold { innerPadding ->
            ComparisonSideBySideLayout(
                original = null,
                modified = null,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
