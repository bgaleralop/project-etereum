package es.bgaleralop.etereum.presentation.common.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import es.bgaleralop.etereum.presentation.theme.TacticalAmber

@Composable
fun ProcessingOverlay(message: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
            .clickable(enabled = false) {}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RadarLoader(size = 100.dp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                color = TacticalAmber,
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun RadarLoader(modifier: Modifier = Modifier, radarColor: Color = TacticalAmber, size: Dp = 80.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarRotation"
    )

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(size.toPx() / 2, size.toPx() / 2)
        val radius = size.toPx() / 2

        // 1. Dibujar circulos concéntricos.
        drawCircle(
            color = radarColor.copy(alpha = 0.2f),
            radius = radius,
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = radarColor.copy(alpha = 0.1f),
            radius = radius * 0.6f,
            style = Stroke(width = 1.dp.toPx())
        )

        // 2. Dibujar lineas de los ejes.
        drawLine(
            color = radarColor.copy(alpha = 0.15f),
            start = Offset(0f, center.y),
            end = Offset(size.toPx(), center.y),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = radarColor.copy(alpha = 0.15f),
            start = Offset(center.x, 0f),
            end = Offset(center.x, size.toPx()),
            strokeWidth = 1.dp.toPx()
        )

        // 3. Dibujar el barrido.
        rotate(rotation, center) {
            drawArc(
                brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    0.5f to radarColor.copy(alpha = 0.4f),
                    1f to radarColor,
                    center = center
                ),
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = true,
                style = Fill
            )
        }

        // 4. Punto Central
        drawCircle(
            color = radarColor,
            radius = 2.dp.toPx(),
            center = center
        )
    }
}


@Preview
@Composable
private fun ProcessingOverlayPreview() {
    EtereumTheme {
        Scaffold { innerPadding ->
            ProcessingOverlay(
                message = "Cargando",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}