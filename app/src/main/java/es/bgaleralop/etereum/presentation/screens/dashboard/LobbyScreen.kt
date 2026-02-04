package es.bgaleralop.etereum.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme

@Composable
fun LobbyScreen(onNavigateToImages: () -> Unit, onNavigateToDocs: () -> Unit, modifier: Modifier = Modifier) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.ScreenPadding)
        ) {
//            // Header estilo Etereum System
//            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "ETEREUM // SYSTEM",
//                    style = MaterialTheme.typography.displayLarge,
//                    color = TacticalAmber
//                )
//                Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
//                    drawLine(
//                        color = TacticalAmber,
//                        start = Offset(0f, 0f),
//                        end = Offset(size.width, 0f),
//                        strokeWidth = 2f
//                    )
//                }
//            }

            Spacer(modifier = Modifier.height(40.dp))

            // Las dos opciones principales
            TacticCard(
                title = "IMAGE OPS",
                subtitle = "Optimization & Metadata Stripping",
                icon = Icons.Filled.CameraAlt,
                onClick = onNavigateToImages
            )

            TacticCard(
                title = "DOC OPS",
                subtitle = "Secure Text Extraction // PDF-TXT",
                icon = Icons.Default.Description,
                statusText = "DATA ENCRYPTION: ACTIVE",
                onClick = onNavigateToDocs
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer de versión
            Text(
                text = "V1.0.0 // bgalera",
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