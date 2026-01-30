package es.bgaleralop.etereum.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import es.bgaleralop.etereum.domain.images.model.OutputFormat
import es.bgaleralop.etereum.domain.images.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream

class ImageRepositoryImpl (
    val context: Context
) : ImageRepository {
    override suspend fun saveImage(
        bitmap: Bitmap,
        fileName: String,
        folder: String,
        format: OutputFormat,
        quality: Int
    ): Result<Uri> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        // Configurar el formato de compresión y la extensión
        val extension = when (format) {
            OutputFormat.WEBP -> "webp"
            OutputFormat.PNG -> "png"
            OutputFormat.JPEG -> "jpeg"
        }

        // Definir la ruta relativa (Carpeta de Mision)
        val relativePath = "Pictures/Etereum/$folder"

        // Configuramos los detalles de la imagen
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.$extension")
            put(MediaStore.Images.Media.MIME_TYPE, "image/$extension")

            // A partir de Android 10, se utiliza RELATIVE_PATH
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                // Bloqueamos el archivo mientras lo guardamos
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val uri = resolver.insert(collection, imageDetails)
            ?: return@withContext Result.failure(Exception("Error al crear el registro en MediaStore"))

        return@withContext try {
            val outputStream: OutputStream? = resolver.openOutputStream(uri)
            outputStream?.use { stream ->
                if(!bitmap.compress(getCompressFormat(format), quality, stream)) {
                   throw Exception("Fallo la compresión del bitmap")
                }
            }

            // Liberar el archivo para que sea visible por el sistema
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageDetails.clear()
                imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, imageDetails, null, null)
            }

            Result.success(uri)
        } catch (e: Exception) {
            // Limpiar si falla
            resolver.delete(uri, null, null)
            Result.failure(e)
        }
    }

    override suspend fun openImage(uri: Uri): Result<Bitmap> {
        TODO("Not yet implemented")
    }

    private fun getCompressFormat(format: OutputFormat): Bitmap.CompressFormat {
        return when(format) {
            OutputFormat.PNG -> Bitmap.CompressFormat.PNG
            OutputFormat.JPEG -> Bitmap.CompressFormat.JPEG
            OutputFormat.WEBP -> Bitmap.CompressFormat.WEBP
        }
    }
}