package es.bgaleralop.etereum.domain.images.usecases

import android.graphics.Bitmap
import android.net.Uri
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Caso de uso para guardar una imagen.
 *
 * @param imageRepository Repositorio de imágenes.
 * @return El resultado de la operación conteniendo la uri del recurso o failed.
 */
class SaveImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    /**
     * Guarda la imagen, con los parametros pasados por atributo.
     */
    suspend operator fun invoke(
        bitmap: Bitmap,
        params: SaveParams
    ): Result<Uri> = withContext(Dispatchers.IO){
        val uri = imageRepository.saveImage(
            bitmap = bitmap,
            fileName = params.fileName,
            folder = params.folder,
            format = params.format,
            quality = params.quality
        )

        uri.onSuccess {
            return@withContext Result.success(it)
        }.onFailure { it ->
            return@withContext Result.failure(Exception("Fallo al guardar la imagen"))
        }
    }
}

/**
 * Clase de datos auxiliar para pasar los parámetros de guardado.
 */
data class SaveParams(
    val fileName: String,
    val folder: String,
    val format: ImageFormat,
    val sanitized: Boolean,
    val quality: Int
)