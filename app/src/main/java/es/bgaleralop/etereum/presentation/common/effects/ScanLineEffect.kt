package es.bgaleralop.etereum.presentation.common.effects

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

fun Modifier.scanLineEffect(): Modifier = this.drawWithContent {
    drawContent()
    val lineCount = 100
    val lineHeight = size.height / lineCount
    for (i in 0 until lineCount) {
        if (i % 2 == 0) {
            drawRect(
                color = Color.Black.copy(alpha = 0.05f),
                topLeft = Offset(0f, i * lineHeight),
                size = Size(size.width, lineHeight)
            )
        }
    }
}