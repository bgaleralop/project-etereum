package es.bgaleralop.etereum.domain.images.model

import android.graphics.Bitmap
import android.os.Build

/**
 * Formato de la imagen
 */
enum class ImageFormat {
    JPEG,
    WEBP,
    PNG
}

fun getCompressFormat(format: ImageFormat): Bitmap.CompressFormat{
    return when (format) {
        ImageFormat.PNG -> Bitmap.CompressFormat.PNG
        ImageFormat.JPEG -> Bitmap.CompressFormat.JPEG
        ImageFormat.WEBP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
    }
}