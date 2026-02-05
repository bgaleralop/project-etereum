package es.bgaleralop.etereum.domain.images.model

import android.graphics.Bitmap

/**
 * Clase de datos que representa una imagen procesada
 */
data class ImageProcessResult(
    val image: Bitmap,
    val weightInBytes: Long,
    val isSanitized: Boolean = false
)