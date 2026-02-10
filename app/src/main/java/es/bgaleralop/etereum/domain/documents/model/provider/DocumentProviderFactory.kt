package es.bgaleralop.etereum.domain.documents.model.provider

import es.bgaleralop.etereum.domain.documents.model.doc.DocType

interface DocumentProviderFactory {
    fun getEncoder(format: DocType): DocumentProvider
    fun getDecoder(format: DocType): DocumentProvider
}