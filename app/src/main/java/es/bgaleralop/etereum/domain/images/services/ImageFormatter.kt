package es.bgaleralop.etereum.domain.images.services

import android.graphics.Bitmap
import android.media.MediaMuxer
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult

/**
 * Recoge las funcionalidades para el formateador de imagen
 */
interface ImageFormatter {

    /**
     * Comprime una imagen.
     *
     * @return ImageProcessResult conteniendo la imagen comprida o failure en caso de fallo.
     */
    suspend fun compress(
        inputBytes: ByteArray,
        quality: Int,
        format: MediaMuxer.OutputFormat
    ): Result<ImageProcessResult>

    /**
     * Convierte una imagen a escala de grises.
     *
     * @param image La imagen a convertir.
     * @return La imagen en escala de grises.
     */
    suspend fun toGrayScale(image: Bitmap): Bitmap

    /**
     * Borra los metadatos de la imagen
     */
    suspend fun sanitize(image: Bitmap): Bitmap

    /**
     * Recorta el tamaño de una imagen.
     */
    suspend fun resize(image: Bitmap, width: Int, height: Int): Bitmap
}