package es.bgaleralop.etereum.domain.documents.model.doc

/**
 * Errores especificos de dominio para UI y Logs
 */
sealed class ETPError(open val message: String) {
    object FileTooLarge : ETPError("El archivo excede el límite operativo (50MB)")
    object EncryptedFile : ETPError("Archivo protegido. Imposible desencriptar.")
    object UnsupportedFormat : ETPError("Formato no soportado por el motor táctico.")
    object ExtractionFailed : ETPError("Fallo crítico en el motor de extracción.")
    data class IOError(override val message: String) : ETPError(message)
}