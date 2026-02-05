package es.bgaleralop.etereum.presentation.screens.imageEdition

import es.bgaleralop.etereum.domain.common.Status
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult

data class ImageEditState(
    val originalBitmap: ImageProcessResult? = null,
    var modifiedBitmap: ImageProcessResult? = null,

    // Parámetros del Panel de Control
    var quality: Float = 0.8f,
    var targetFormat: ImageFormat = ImageFormat.WEBP,
    var isGrayScale: Boolean = false,
    var shouldSanitize: Boolean = true,

    // Gestión de Archivos.
    var outputName: String = "",
    var targetDirectory: String = "",

    // Estado de la transformación.
    var imageStatus: Status = Status.IDLE,

    // Formato de visualizacion
    var isForcedSlider: Boolean = false
) {
    val savingPercentage: Int
        get() = if(modifiedBitmap != null && originalBitmap != null) {
            if(originalBitmap.weightInBytes > 0) {
                (100 - (modifiedBitmap!!.weightInBytes * 100)).toInt().coerceAtLeast(0)
            } else 0
        } else 0
}
