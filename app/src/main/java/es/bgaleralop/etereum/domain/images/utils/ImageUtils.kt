package es.bgaleralop.etereum.domain.images.utils

import es.bgaleralop.etereum.domain.common.Status


fun determineIsEnabledByStatus(status: Status): Boolean = when (status) {
    Status.IDLE -> true
    Status.PROCESSING -> false
    Status.COMPLETED -> true
    Status.ERROR -> true
}