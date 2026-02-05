package es.bgaleralop.etereum.presentation.screens.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
){
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimensions.ScreenPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.configuracion),
            style = MaterialTheme.typography.titleLarge,
            color = TacticalAmber,
            modifier = Modifier.padding(bottom = Dimensions.PaddingMedium)
        )

        SettingsSection(title = stringResource(R.string.interfaz)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.preferSliderMode, onCheckedChange = { viewModel.onPreferSliderModeChange(it) })
                Text(text = stringResource(R.string.siempre_modo_slider), style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.PaddingMedium))

        SettingsSection(title = stringResource(R.string.almacenamiento)) {
            SettingsTextField(
                label = stringResource(R.string.carpeta_almacenamiento),
                value = state.lastMissionFolder,
                onValueChange = { viewModel.onLastMissionFolderChange(it) }
            )
        }

        SettingsSwitch(
            label = stringResource(R.string.borrar_original),
            checked = state.deleteOriginal,
            onCheckedChange = { viewModel.onDeleteOriginalChange(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Información de Versión
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(
                stringResource(R.string.version),
                style = MaterialTheme.typography.labelSmall,
                color = TacticalAmber.copy(alpha = 0.3f)
            )
        }
    }

}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            SettingsScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}