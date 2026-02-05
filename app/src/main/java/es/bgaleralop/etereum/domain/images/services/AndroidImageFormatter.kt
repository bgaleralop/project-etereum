package es.bgaleralop.etereum.domain.images.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import androidx.core.graphics.createBitmap
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.model.getCompressFormat
import es.bgaleralop.etereum.domain.images.utils.Rectangle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Implementacion de ImageFormatter para Android
 */
class AndroidImageFormatter : ImageFormatter {
    private val TAG = "ETEREUM ImageFormatter: "

    override suspend fun compress(
        bitmap: Bitmap,
        quality: Int,
        format: ImageFormat
    ): Result<ImageProcessResult> = withContext(Dispatchers.Default) {
        Log.d(TAG, "Comprimiendo imagen $bitmap a formato ${format.name} y calidad $quality%")

        val compressFormat = getCompressFormat(format)

        val outputStream = ByteArrayOutputStream()

        bitmap.compress(compressFormat, quality, outputStream)

        val imageBytes = outputStream.toByteArray()

        // Devolvemos el bitmap procesado.
        Result.success(ImageProcessResult(
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size),
            weightInBytes = outputStream.toByteArray().size.toLong(),
            isSanitized = false
        ))
    }

    override suspend fun toGrayScale(image: Bitmap): Bitmap {
        Log.d(TAG, "Convirtiendo a escala de grises")

        Log.d("ETEREUM DEPURADOR: SOLUCIONANDO FALLO NULLPOINTEREXCEPTION", "BITMAP: $image")

        val width = image.width
        val height = image.height
        val bmpGrayscale = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmpGrayscale)
        val paint = Paint()

        // Con ColorMatrix con saturación 0 eliminamos toda la información de color
        val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        canvas.drawBitmap(image, 0f, 0f, paint)

        return bmpGrayscale
    }

    override suspend fun sanitize(image: Bitmap): Bitmap {
        return image
    }

    override suspend fun resize(
        image: Bitmap,
        rectangle: Rectangle
    ): Bitmap {
        Log.d(TAG, "Recortando imagen")

        val resizedBitmap = Bitmap.createBitmap(
            image,
            rectangle.x.coerceAtLeast(0),
            rectangle.y.coerceAtLeast(0),
            rectangle.width.coerceIn(1, image.width - rectangle.x),
            rectangle.height.coerceIn(1, image.height - rectangle.y)
        )

        return resizedBitmap
    }
}

fun Bitmap.toRawByteArray(): ByteArray {
    val size = this.byteCount
    val buffer = ByteBuffer.allocate(size)

    // 1. Aseguramos que el buffer esté al inicio
    buffer.rewind()

    // 2. Copiamos los píxeles
    this.copyPixelsToBuffer(buffer)

    // 3. ¡IMPORTANTE! Retrocedemos el puntero al inicio antes de leer
    buffer.rewind()

    // 4. Extraemos los bytes de forma segura
    val byteArray = ByteArray(size)
    buffer.get(byteArray)

    return byteArray
}