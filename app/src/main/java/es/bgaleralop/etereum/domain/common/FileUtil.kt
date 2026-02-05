package es.bgaleralop.etereum.domain.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream

/**
 * Función para obtener tamaño de una URI.
 *
 * @param context Contexto de la aplicación.
 * @param uri URI del archivo.
 * @return Tamaño del archivo en bytes.
 */
fun getUriSize(context: Context, uri: Uri): Long {
    return context.contentResolver.openAssetFileDescriptor(uri, "r")?.use {
        it.length
    } ?: 0L
}

/**
 * Función para calcular el tamaño de compresión de una imagen.
 *
 * @param outputStream Stream de salida para la imagen comprimida.
 * @return Tamaño de compresión en bytes.
 */
fun Bitmap.calculateCompressedSize(outputStream: ByteArrayOutputStream): Long {
    return outputStream.toByteArray().size.toLong()
}

/**
 * Función para formatear el tamaño de un archivo.
 *
 * @param bytes Tamaño del archivo en bytes.
 * @return Tamaño del archivo formateado.
 */
fun formatSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.2f MB", bytes / (1024 * 1024.0))
    }
}

/**
 * Función para obtener el nombre de un archivo a partir de su URI.
 *
 * @param context Contexto de la aplicación.
 * @param uri URI del archivo.
 * @return Nombre del archivo.
 */
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = "unknown_file"

    // Si la URI es de tipo 'content' (lo normal al elegir de la galeria)
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                // Buscamos la columna DisplayName
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
    } else if (uri.scheme == "file") {
        name = uri.lastPathSegment ?: "unknown_file"
    }

    return name
}