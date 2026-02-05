package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun EditScreen(
    state: ImageEditState,
    onAction: (ImageAction) -> Unit,
    modifier: Modifier
) {
    val TAG = "ETEREUM EditScreen: "
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTablet = configuration.screenWidthDp >= 600

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAction(ImageAction.LoadImage(it)) }
    }
    if(state.originalBitmap == null) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = TacticalAmber.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = TacticalAmber)
            ) {
                Text("IMPORTAR IMAGEN", color = MaterialTheme.colorScheme.background)
            }
        }
    } else {

        Log.i(TAG, "Comprobando Orientación...")
        if(isPortrait) {
            Log.d(TAG, "Orientación: Portrait")
            // OPCION 1: MOVIL PORTRAIT (Vertical)
            Column(modifier = modifier.fillMaxSize()) {
                WeightComparisonBadge(state = state, savingPercentaje = state.savingPercentage)
                Box(modifier = Modifier.weight(1.2f)) {
                    ComparisonSliderLayout(original = state.originalBitmap?.image, modified = state.modifiedBitmap?.image)
                }
                ImageControlPanel(
                    state = state,
                    onAction = onAction,
                )
            }
        } else {
            Log.d(TAG, "Orientación: Landscape")
            // OPCION 2: MOVIL LANDSCAPE (Horizontal) O TABLET
            Column(modifier = modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxSize().weight(1.2f)) {
                    Box(modifier = Modifier.weight(1.2f)) {
                        if (state.isForcedSlider){
                            WeightComparisonBadge(state = state, savingPercentaje = state.savingPercentage, modifier = Modifier.zIndex(2f))
                            ComparisonSliderLayout(original = state.originalBitmap?.image, modified = state.modifiedBitmap?.image)
                        } else {
                            WeightComparisonBadge(state = state, savingPercentaje = state.savingPercentage, modifier = Modifier.zIndex(2f))
                            ComparisonSideBySideLayout(original = state.originalBitmap?.image, modified = state.modifiedBitmap?.image)
                        }
                    }

                    ImageControlPanel(
                        state = state,
                        onAction = onAction,
                        isPortrait = false,
                        modifier = Modifier.weight(0.7f)
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun EditScreenPreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            EditScreen(ImageEditState(), onAction = {}, modifier = Modifier.fillMaxSize().padding(innerPadding))
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
            EditScreen(ImageEditState(), onAction = {}, modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}