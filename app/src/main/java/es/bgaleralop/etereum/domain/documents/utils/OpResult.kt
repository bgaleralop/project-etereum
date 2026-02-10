package es.bgaleralop.etereum.domain.documents.utils

import es.bgaleralop.etereum.domain.documents.model.doc.ETPError

/**
 * Wrapper para manejo de errores funcional
 */
sealed class OpResult<out T> {
    data class Success<out T>(val data: T) : OpResult<T>()
    data class Failure(val error: ETPError) : OpResult<Nothing>()
}