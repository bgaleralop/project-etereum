package es.bgaleralop.etereum.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.SurfaceGreyVariant

@Composable
fun MainButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth(0.7f)) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun SecondaryButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){
    if (enabled) {
        OutlinedButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = modifier.fillMaxWidth(0.7f),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium.copy(
                color = SurfaceGreyVariant
            ), fontWeight = FontWeight.Bold)
        }
    }
}





@Preview(showSystemUi = true, showBackground = true,
    device = "spec:parent=pixel_5, navigation=buttons",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ButtonsPreview(){
    EtereumTheme {
        Scaffold { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                MainButton(title = "Abrir imagen", onClick = {})
                SecondaryButton(title = "Guardar", onClick = {}, enabled = true)
            }
        }
    }
}