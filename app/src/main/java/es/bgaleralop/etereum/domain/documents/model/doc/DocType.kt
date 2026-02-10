package es.bgaleralop.etereum.domain.documents.model.doc

/**
 * Enumeracion que recoge los tipos de archivos soportados por la aplicacion.
 */
enum class DocType(val mime: String, extension: String) {
    PDF("application/pdf", "pdf"),
    WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    ETEREUM("application/pdf", "ete"),
    UNKNOWN("application/octet-stream", "bin");

    companion object {
        fun fromMime(mime: String?) : DocType =
            entries.find { it.mime == mime } ?: UNKNOWN
    }
}