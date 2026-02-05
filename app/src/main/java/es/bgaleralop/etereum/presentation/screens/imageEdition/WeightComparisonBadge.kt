package es.bgaleralop.etereum.presentation.screens.imageEdition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.bgaleralop.etereum.domain.common.formatSize
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun WeightComparisonBadge(
    state: ImageEditState,
    savingPercentaje: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimensions.PaddingSmall)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
            .border(1.dp, TacticalAmber.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("ORIGINAL", style = MaterialTheme.typography.labelSmall, color = TacticalAmber.copy(alpha = 0.5f))
            Text(formatSize(state.originalBitmap?.weightInBytes ?: 0), style = MaterialTheme.typography.bodyMedium, color = TacticalAmber)

        }
        // Icono de flecha
        Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingDown,
            contentDescription = null,
            tint = TacticalAmber,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Column(horizontalAlignment = Alignment.End) {
            Text("OPTIMIZADO (-$savingPercentaje%)", style = MaterialTheme.typography.labelSmall, color = TacticalAmber.copy(alpha = 0.5f))
            Text(formatSize(state.modifiedBitmap?.weightInBytes ?: 0), style = MaterialTheme.typography.bodyMedium, color = TacticalAmber)
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComparisonBadgePreview() {
    EtereumTheme {
        WeightComparisonBadge(
            state = ImageEditState(),
            savingPercentaje = 67
        )
    }
}