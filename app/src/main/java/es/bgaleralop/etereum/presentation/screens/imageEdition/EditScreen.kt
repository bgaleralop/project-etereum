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
fun EditScreen(
    state: ImageEditState,
    onAction: (ImageAction) -> Unit,
    modifier: Modifier
) {
    val TAG = "ETEREUM EditScreen: "
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTablet = configuration.screenWidthDp >= 600

    Log.i(TAG, "Comprobando Orientación...")
    if(isPortrait) {
        Log.d(TAG, "Orientación: Portrait")
        // OPCION 1: MOVIL PORTRAIT (Vertical)
        Column(modifier = modifier.fillMaxSize()) {
            WeightComparisonBadge(state = state)
            Box(modifier = Modifier.weight(1.2f)) {
                ComparisonSliderLayout(original = state.originalBitmap?.image, modified = state.modifiedBitmap?.image)
            }
            ImageControlPanel(
                state = state,
                onAction = onAction
            )
        }
    } else {
        Log.d(TAG, "Orientación: Landscape")
        // OPCION 2: MOVIL LANDSCAPE (Horizontal) O TABLET
        Column(modifier = modifier.fillMaxSize()) {
            /*Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.isForcedSlider, onCheckedChange = {onAction(ImageAction.ToogleSliceMode)} )
                Text(
                    text = stringResource(R.string.slice_mode),
                    style = MaterialTheme.typography.labelMedium)
            }*/
            Row(modifier = Modifier.fillMaxSize().weight(1.2f)) {
                Box(modifier = Modifier.weight(1.2f)) {
                    if (state.isForcedSlider){
                        WeightComparisonBadge(state = state)
                        ComparisonSliderLayout(original = state.originalBitmap?.image, modified = state.modifiedBitmap?.image)
                    } else {
                        WeightComparisonBadge(state = state)
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