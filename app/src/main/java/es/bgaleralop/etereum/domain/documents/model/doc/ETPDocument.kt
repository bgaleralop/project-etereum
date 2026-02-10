package es.bgaleralop.etereum.domain.documents.model.doc

data class ETPDocument(
    val metadata: ETPMetadata,
    val content: List<ETPBlock>
) {
}