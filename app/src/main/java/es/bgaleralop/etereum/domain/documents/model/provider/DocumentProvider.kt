package es.bgaleralop.etereum.domain.documents.model.provider

import es.bgaleralop.etereum.domain.documents.model.doc.DocType
import es.bgaleralop.etereum.domain.documents.model.doc.ETPBlock
import es.bgaleralop.etereum.domain.documents.model.doc.ETPDocument
import java.io.InputStream
import java.io.OutputStream

interface DocumentProvider {
    val supportedType: DocType
    suspend fun encode(inputStream: InputStream): List<ETPBlock>
    suspend fun decode(document: ETPDocument): OutputStream
}