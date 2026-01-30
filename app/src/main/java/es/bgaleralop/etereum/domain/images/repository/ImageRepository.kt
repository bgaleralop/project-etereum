package es.bgaleralop.etereum.domain.images.repository

import android.graphics.Bitmap
import android.net.Uri
import es.bgaleralop.etereum.domain.images.model.ImageFormat

interface ImageRepository {
    /**
     * Guarda una imagen en la carpeta de la aplicación.
     *
     * @param bitmap la imagen a guardar.
     * @param fileName el nombre de la imagen
     * @param folder la carpeta de misión contenedora
     * @param format el formato de la imagen
     * @param quality la calidad de la imagen
     *
     * @return La Uri del recurso guardado o failure en caso de fallo
     */
    suspend fun saveImage(
        bitmap: Bitmap,
        fileName: String,
        folder: String,
        format: ImageFormat,
        quality: Int
    ): Result<Uri>

    /**
     * Obtiene una imagen desde el sistema de archivos
     *
     * @param uri La Uri de la imagen
     *
     * @return La imagen o un failure en caso de fallo
     */
    suspend fun openImage(uri: Uri): Result<Bitmap>
}