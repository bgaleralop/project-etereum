package es.bgaleralop.etereum.presentation.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.etereum.presentation.theme.Dimensions
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber
import es.bgaleralop.etereum.presentation.theme.TacticalGreen

@Composable
fun TacticCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    statusText: String = "STATUS: OPERATIONAL",
    onClick: () -> Unit
) {
    Card (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = Dimensions.PaddingSmall),
        shape = CutCornerShape(topStart = Dimensions.CardCornerRadius, bottomEnd = Dimensions.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(TacticalAmber, Color.Transparent)))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(Dimensions.ControlSpacing)) {
            Column(horizontalAlignment = Alignment.Start) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TacticalAmber,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge,
                    color = TacticalAmber
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TacticalAmber.copy(alpha = 0.7f)
                )
            }

            // Texto de estado en la parte inferior (estilo terminal)
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
                color = TacticalGreen,
                modifier = Modifier.align(Alignment.BottomStart)
            )

            // Decoración de esquina
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TacticalAmber.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
            )
        }
    }
}


@Preview
@Composable
fun TacticCardPreview(){
    EtereumTheme {
        TacticCard(
            title = "IMAGE OPS",
            subtitle = "Optimización & Metadata Stripping",
            icon = Icons.Default.Build,
            onClick = {}
        )
    }
}