package es.bgaleralop.etereum.presentation.screens.imageEdition.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.domain.common.Status
import es.bgaleralop.etereum.domain.images.utils.determineIsEnabledByStatus
import es.bgaleralop.etereum.presentation.common.components.MainButton
import es.bgaleralop.etereum.presentation.common.components.SecondaryButton
import es.bgaleralop.etereum.presentation.screens.imageEdition.ImageAction
import es.bgaleralop.etereum.presentation.screens.imageEdition.ImageEditState
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.SurfaceGrey
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun ImageControlPanel(
    state: ImageEditState,
    onAction: (ImageAction) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    isPortrait: Boolean = true
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAction(ImageAction.LoadImage(it)) }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceGrey),
        modifier = modifier
            .padding(Dimensions.ScreenPadding)
            .alpha(if (isEnabled) 1f else 0.5f)
    ) {
        var sliderPosition by rememberSaveable { mutableFloatStateOf(state.quality) }

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.ControlSpacing),
            modifier = Modifier
                .padding(Dimensions.ControlSpacing)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. GESTION DE ARCHIVO.
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.output_configuration), style = MaterialTheme.typography.labelMedium)
                EditOverflowMenu(
                    isGrayEscale = state.isGrayScale,
                    onAction = onAction,
                    enabled = determineIsEnabledByStatus(state.imageStatus)
                )
            }
            OutlinedTextField(
                value = state.outputName,
                onValueChange = { onAction(ImageAction.UpdateName(it)) },
                label = { Text(text = stringResource(R.string.file_name)) },
                enabled = isEnabled,
                modifier = Modifier.fillMaxWidth()
            )

            // 2. CALIDAD.
            Text(text = stringResource(R.string.quality, (sliderPosition * 100).toInt()), style = MaterialTheme.typography.labelMedium)
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = { onAction(ImageAction.UpdateQuality(sliderPosition)) },
                enabled = isEnabled
            )

            // 3. OPCIONES TÁCTICAS.
            Column {

//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(checked = state.isGrayScale, onCheckedChange = { onAction(ImageAction.ToggleGrayScale) }, enabled = isEnabled)
//                    Text(text = stringResource(R.string.gray_scale), style = MaterialTheme.typography.labelMedium)
//                }
                if(!isPortrait){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = state.isForcedSlider, onCheckedChange = { onAction(
                            ImageAction.ToogleSliceMode) }, enabled = isEnabled)
                        Text(text = stringResource(R.string.slice_mode), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // 4. ACCIONES FINALES
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.PaddingSmall),
            ) {
                MainButton(
                    title = stringResource(R.string.save),
                    onClick = { onAction(ImageAction.SaveAndOpen) },
                    enabled = isEnabled,
                    modifier = modifier.weight(0.5f)
                )
                SecondaryButton(
                    title = stringResource(R.string.open),
                    onClick = { launcher.launch("image/*") },
                    enabled = isEnabled,
                    modifier = modifier.weight(0.3f)
                )
            }
            Spacer(Modifier.padding(top = Dimensions.PaddingSmall))
        }
    }
}


/**
 * Componente para mostrar el menú de opciones de la imagen.
 *
 */
@Composable
private fun EditOverflowMenu(
    isGrayEscale: Boolean,
    onAction: (ImageAction) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { expanded = true }, enabled = enabled) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones extra",
                tint = TacticalAmber
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.gray_scale), color = MaterialTheme.colorScheme.onSurface) },
                leadingIcon = {
                    Icon(
                        imageVector = if (isGrayEscale) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = TacticalAmber
                    )
                },
                onClick = {
                    onAction(ImageAction.ToggleGrayScale)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Recortar Imagen", color = MaterialTheme.colorScheme.onSurface) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Crop,
                        contentDescription = null,
                        tint = TacticalAmber
                    )
                },
                onClick = {
                    onAction(ImageAction.OpenCroopTool)
                    expanded = false
                }
            )
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ImageControlPanelPreview(){
    EtereumTheme {
        Scaffold { innerPadding ->
            ImageControlPanel(
                state = ImageEditState(imageStatus = Status.COMPLETED),
                onAction = {},
                isEnabled = determineIsEnabledByStatus(Status.COMPLETED),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding))
        }
    }
}