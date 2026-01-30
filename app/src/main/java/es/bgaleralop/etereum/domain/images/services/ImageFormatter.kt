package es.bgaleralop.etereum.domain.images.services

import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.utils.Rectangle

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
        format: ImageFormat
    ): Result<ImageProcessResult>

    /**
     * Convierte una imagen a escala de grises.
     *
     * @param image La imagen a convertir.
     * @return La imagen en escala de grises.
     */
    suspend fun toGrayScale(image: ByteArray): ByteArray

    /**
     * Borra los metadatos de la imagen
     */
    suspend fun sanitize(image: ByteArray): ByteArray

    /**
     * Recorta el tamaño de una imagen.
     *
     * @param image La imagen a recortar.
     * @param rectangle El rectángulo con las coordenadas de píxeles(x, y, ancho, alto)
     */
    suspend fun resize(image: ByteArray, rectangle: Rectangle): ByteArray
}