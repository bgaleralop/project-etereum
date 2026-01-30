package es.bgaleralop.etereum.domain.images.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
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
    override suspend fun compress(
        inputBytes: ByteArray,
        quality: Int,
        format: ImageFormat
    ): Result<ImageProcessResult> = withContext(Dispatchers.Default) {

        val compressFormat = getCompressFormat(format)

        val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

        val outputStream = ByteArrayOutputStream()

        bitmap.compress(compressFormat, quality, outputStream)

        val imageBytes = outputStream.toByteArray()

        // Devolvemos el bitmap procesado.
        Result.success(ImageProcessResult(
            image = imageBytes,
            weightInKb = imageBytes.size / 1024,
            isSanitized = false
        ))
    }

    override suspend fun toGrayScale(image: ByteArray): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        val width = bitmap.width
        val height = bitmap.height
        val bmpGrayscale = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmpGrayscale)
        val paint = Paint()

        // Con ColorMatrix con saturación 0 eliminamos toda la información de color
        val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return bmpGrayscale.toRawByteArray()
    }

    override suspend fun sanitize(image: ByteArray): ByteArray {
        return image
    }

    override suspend fun resize(
        image: ByteArray,
        rectangle: Rectangle
    ): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)

        val resizedBitmap = Bitmap.createBitmap(
            bitmap,
            rectangle.x.coerceAtLeast(0),
            rectangle.y.coerceAtLeast(0),
            rectangle.width.coerceIn(1, bitmap.width - rectangle.x),
            rectangle.height.coerceIn(1, bitmap.height - rectangle.y)
        )

        return resizedBitmap.toRawByteArray()
    }
}

fun Bitmap.toRawByteArray(): ByteArray {
    // Calculamos el tamaño: cada pixel en ARGB_8888 ocupa 4 bytes
    val size = this.byteCount
    val buffer = ByteBuffer.allocate(size)

    // Copiamos lso pixeles del bitmap al buffer
    this.copyPixelsToBuffer(buffer)

    return buffer.array()
}