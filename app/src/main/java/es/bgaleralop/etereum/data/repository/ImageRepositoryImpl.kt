package es.bgaleralop.etereum.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.bgaleralop.etereum.domain.images.model.ImageFormat
import es.bgaleralop.etereum.domain.images.repository.ImageRepository
import es.bgaleralop.etereum.domain.images.services.toRawByteArray
import es.bgaleralop.etereum.domain.images.utils.MAX_HEIGHT
import es.bgaleralop.etereum.domain.images.utils.MAX_WIDTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : ImageRepository {
    override suspend fun saveImage(
        bitmap: ByteArray,
        fileName: String,
        folder: String,
        format: ImageFormat,
        quality: Int
    ): Result<Uri> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        // Configurar la extensión
        val extension = when (format) {
            ImageFormat.WEBP -> "webp"
            ImageFormat.PNG -> "png"
            ImageFormat.JPEG -> "jpeg"
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

    override suspend fun openImage(uri: Uri): Result<ByteArray> = withContext(Dispatchers.IO) {
        return@withContext try {
            val resolver = context.contentResolver
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true // Solo leemos las dimensiones.
            }

            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
            options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)
            options.inJustDecodeBounds = false

            // Abrimos el flujo de entrada
            val inputStream = resolver.openInputStream(uri)
                ?: throw Exception("No se puede abrir el archivo")

            inputStream.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                if (bitmap != null) {
                    Result.success(bitmap.toRawByteArray())
                } else {
                    Result.failure(Exception("Error al decodificar la imagen: Formato no soportado"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if(height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calcula el valor más grande de inSampleSize que sea potencia de 2
            // y mantenga tanto la altura como la anchura mayores que las requeridas.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}