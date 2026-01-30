package es.bgaleralop.etereum.domain.common

/**
 * Estado en el que se encuentra el proceso de codificación
 */
enum class Status {
    IDLE,
    PROCESSING,
    COMPLETED,
    ERROR
}