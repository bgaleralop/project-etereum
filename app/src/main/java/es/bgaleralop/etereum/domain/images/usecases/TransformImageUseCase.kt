package es.bgaleralop.etereum.domain.images.usecases

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.services.ImageFormatter
import es.bgaleralop.etereum.domain.images.services.toRawByteArray
import es.bgaleralop.etereum.domain.images.utils.Rectangle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Caso de uso para transformar una imagen.
 */
class TransformImageUseCase @Inject constructor(
    private val formatter: ImageFormatter
) {
    suspend fun compressImage(
        bitmap: Bitmap,
        quality: Int,
        isGrayScale: Boolean,
        format: ImageFormat = ImageFormat.WEBP
    ): Result<ImageProcessResult> = withContext(Dispatchers.Default) {
        // 1. Creamos el byteArray de la imagen dependiendo de la escala de grises
        val byteArray = if (isGrayScale) {
            formatter.toGrayScale(bitmap.toRawByteArray())
        } else {
            bitmap.toRawByteArray()
        }

        // 2. Comprimimos la imagen
        val result = formatter.compress(
            inputBytes = byteArray,
            quality = quality,
            format = format
        )

        return@withContext result
    }

    /**
     * Recorta una imagen.
     *
     * @param bitmap La imagen a recortar.
     * @param left La coordenada x del punto superior izquierdo del recorte.
     * @param top La coordenada y del punto superior izquierdo del recorte.
     * @param width El ancho del recorte.
     * @param height La altura del recorte.
     * @return El resultado de la operación.
     */
    suspend fun resizeImage(
        bitmap: Bitmap,
        left: Int,
        top: Int,
        width: Int,
        height: Int
    ): Result<Bitmap> = withContext(Dispatchers.Default) {
        val byteArray = bitmap.toRawByteArray()

        val result = formatter.resize(
            image = byteArray,
            rectangle = Rectangle(left, top, width, height)
        )

        if (result.isNotEmpty()) {
            return@withContext Result.success(BitmapFactory.decodeByteArray(result, 0, result.size))
        } else {
            return@withContext Result.failure(Exception("Error al recortar la imagen"))
        }
    }
}