package es.bgaleralop.etereum.presentation.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme

@Composable
fun LobbyScreen(
    onNavigateToImages: () -> Unit,
    onNavigateToDocs: () -> Unit,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = configuration.screenWidthDp >= 600

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimensions.ScreenPadding)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        if (isLandscape || isTablet) {
            // Layout Horizontal
            Row(
                modifier = Modifier.fillMaxSize().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TacticCard(
                        title = "IMAGE OPS",
                        subtitle = "Optimization & Metadata Stripping",
                        icon = Icons.Filled.CameraAlt,
                        onClick = onNavigateToImages,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TacticCard(
                        title = "DOC OPS",
                        subtitle = "Secure Text Extraction // PDF-TXT",
                        icon = Icons.Default.Description,
                        statusText = "DATA ENCRYPTION: ACTIVE",
                        onClick = onNavigateToDocs,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium)
            ) {
                TacticCard(
                    title = "IMAGE OPS",
                    subtitle = "Optimization & Metadata Stripping",
                    icon = Icons.Filled.CameraAlt,
                    onClick = onNavigateToImages,
                )
                TacticCard(
                    title = "DOC OPS",
                    subtitle = "Secure Text Extraction // PDF-TXT",
                    icon = Icons.Default.Description,
                    statusText = "DATA ENCRYPTION: ACTIVE",
                    onClick = onNavigateToDocs,
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
        // Footer de versión
        Text(
            text = stringResource(R.string.version),
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyScreenPreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            LobbyScreen( {}, {}, modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true,
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun LobbyScreenLandscapePreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            LobbyScreen( {}, {}, modifier = Modifier.padding(innerPadding)
            )
        }
    }
}