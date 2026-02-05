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
        val image = imageRepository.openImage(uri)

        return@withContext if (image.isSuccess) {
            val bitmap = BitmapFactory.decodeByteArray(image.getOrNull(), 0, image.getOrNull()!!.size)
            Result.success(ImageProcessResult(bitmap, getUriSize(context, uri), isSanitized = false))
        } else {
            Result.failure(Exception("Error al abrir la imagen"))
        }
    }
}