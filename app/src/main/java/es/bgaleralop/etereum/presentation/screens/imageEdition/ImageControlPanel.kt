package es.bgaleralop.etereum.presentation.screens.imageEdition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.presentation.common.components.MainButton
import es.bgaleralop.etereum.presentation.common.components.SecondaryButton
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.SurfaceGrey

@Composable
fun ImageControlPanel(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceGrey),
        modifier = modifier.padding(Dimensions.ScreenPadding)
    ) {
        var outputName by rememberSaveable { mutableStateOf("") }
        var quality by rememberSaveable { mutableFloatStateOf(0.7f) }
        var sliderPosition by rememberSaveable { mutableFloatStateOf(quality) }

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.ControlSpacing),
            modifier = Modifier
                .padding(Dimensions.ControlSpacing)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. GESTION DE ARCHIVO.
            Text(stringResource(R.string.output_configuration), style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = outputName,
                onValueChange = {outputName = it},
                label = { Text(text = stringResource(R.string.file_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            // 2. CALIDAD.
            Text(text = stringResource(R.string.quality, (quality * 100).toInt()), style = MaterialTheme.typography.labelMedium)
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = { }
            )

            // 3. OPCIONES TÁCTICAS.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = true, onCheckedChange = { })
                Text(text = stringResource(R.string.gray_scale), style = MaterialTheme.typography.labelMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = true, onCheckedChange = { })
                Text(text = stringResource(R.string.sanitize), style = MaterialTheme.typography.labelMedium)
            }

            // 4. ACCIONES FINALES
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.PaddingSmall)
            ) {
                MainButton(
                    title = stringResource(R.string.save),
                    onClick = { },
                    modifier.weight(0.5f)
                )
                SecondaryButton(
                    title = stringResource(R.string.open),
                    onClick = {},
                    modifier.weight(0.3f)
                )
            }
        }
    }
}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ImageControlPanelPreview(){
    EtereumTheme {
        Scaffold { innerPadding ->
            ImageControlPanel(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding))
        }
    }
}