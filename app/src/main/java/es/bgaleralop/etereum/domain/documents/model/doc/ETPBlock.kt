package es.bgaleralop.etereum.domain.documents.model.doc

/**
 * Representa un bloque de información del documento.
 */
sealed class ETPBlock {
    data class Header(val text: String, val level: Int?) : ETPBlock()
    data class Paragraph(val text: String) : ETPBlock()
    data class BulletList(val items: List<String>) : ETPBlock()
    data class NumberedList(val items: List<String>) : ETPBlock()
    data class TableRow(val cells: List<String>) : ETPBlock()
    data class Table(val columns: Int, val rows: List<TableRow>) : ETPBlock()
}