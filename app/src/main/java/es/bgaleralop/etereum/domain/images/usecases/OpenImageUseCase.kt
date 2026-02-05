package es.bgaleralop.etereum.domain.images.usecases

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import es.bgaleralop.etereum.domain.common.getUriSize
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<ImageProcessResult> = withContext(Dispatchers.IO) {
        try {
            // 1. Log de diagnóstico (como los que vimos)
            println("ETEREUM_DEBUG: Iniciando decodificación segura para $uri")

            // 2. Abrir el stream directamente (evita el NPE de los bytes nulos)
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return@withContext Result.failure(Exception("Stream nulo: No se pudo acceder al archivo"))

            inputStream.use { stream ->
                // 3. Decodificación segura sin operadores !!
                val bitmap = BitmapFactory.decodeStream(stream)
                    ?: return@withContext Result.failure(Exception("Bitmap nulo: El archivo no es una imagen válida"))

                // 4. Obtener tamaño (usa la función de utilidad que definimos)
                val size = getUriSize(context, uri)

                Result.success(
                    ImageProcessResult(
                        image = bitmap,
                        weightInBytes = size,
                        isSanitized = false
                    )
                )
            }
        } catch (e: Exception) {
            println("ETEREUM_ERROR: Fallo crítico en UseCase: ${e.message}")
            Result.failure(e)
        }
    }
}