package es.bgaleralop.etereum.domain.documents.repository

import android.net.Uri
import es.bgaleralop.etereum.domain.documents.model.doc.ETPDocument
import es.bgaleralop.etereum.domain.documents.utils.OpResult

interface DocsRepository {
    suspend fun loadEtpFile(uri: Uri): OpResult<ETPDocument>
    suspend fun saveEtpFile(document: ETPDocument, fileName: String, folder: String): OpResult<Uri>
}