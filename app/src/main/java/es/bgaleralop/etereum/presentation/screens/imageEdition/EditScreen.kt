package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import es.bgaleralop.etereum.presentation.theme.EtereumTheme

@Composable
fun EditScreen(modifier: Modifier) {
    val TAG = "ETEREUM EditScreen: "
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTablet = configuration.screenWidthDp >= 600

    Log.i(TAG, "Comprobando Orientación...")
    if(isPortrait) {
        Log.d(TAG, "Orientación: Portrait")
        // OPCION 1: MOVIL PORTRAIT (Vertical)
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1.2f)) {
                ComparisonSliderLayout(null, null)
            }
            ImageControlPanel()
        }
    } else {
        Log.d(TAG, "Orientación: Landscape")
        // OPCION 2: MOVIL LANDSCAPE (Horizontal) O TABLET
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1.2f)) {
                ComparisonSideBySideLayout(null, null)
            }
            ImageControlPanel(modifier = Modifier.weight(0.7f))
        }
    }
}



@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun EditScreenPreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            EditScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "LandScape",
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun EditScreenPreviewLandScape() {
    EtereumTheme {
        Scaffold { innerPadding ->
            EditScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}