package es.bgaleralop.etereum.domain.images.usecases

import android.graphics.Bitmap
import android.util.Log
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.services.ImageFormatter
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
        Log.d("ETEREUM DEPURADOR: SOLUCIONANDO FALLO NULLPOINTEREXCEPTION", "bitmap: $bitmap")
        val image = if (isGrayScale) {
            formatter.toGrayScale(bitmap)
        } else {
            bitmap
        }

        // 2. Comprimimos la imagen
        val result = formatter.compress(
            bitmap = image,
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

        val result = formatter.resize(
            image = bitmap,
            rectangle = Rectangle(left, top, width, height)
        )

        return@withContext Result.success(result)
    }
}