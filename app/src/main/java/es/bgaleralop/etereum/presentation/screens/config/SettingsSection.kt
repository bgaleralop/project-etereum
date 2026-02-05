package es.bgaleralop.etereum.presentation.screens.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title.uppercase(), color = TacticalAmber.copy(alpha = 0.6f), style = MaterialTheme.typography.labelMedium)
        HorizontalDivider(color = TacticalAmber.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
        content()
    }
}

@Composable
fun SettingsSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TacticalAmber,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TacticalAmber,
                checkedTrackColor = TacticalAmber.copy(alpha = 0.3f),
                uncheckedThumbColor = TacticalAmber.copy(alpha = 0.4f),
                uncheckedTrackColor = androidx.compose.ui.graphics.Color.Black,
                uncheckedBorderColor = TacticalAmber.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
fun SettingsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TacticalAmber.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TacticalAmber),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TacticalAmber,
                unfocusedBorderColor = TacticalAmber.copy(alpha = 0.3f),
                cursorColor = TacticalAmber,
                focusedContainerColor = TacticalAmber.copy(alpha = 0.05f)
            ),
            placeholder = {
                Text("Carpeta destino...", color = TacticalAmber.copy(alpha = 0.2f))
            }
        )
    }
}