package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.net.Uri
import es.bgaleralop.etereum.domain.images.model.ImageFormat

/**
 * Conjunto de acciones posibles que puede ejecutar el usuario.
 */
sealed interface ImageAction {
    // Acciones de Configuracion (Cambios en el Panel de Control)
    data class UpdateName(val newName: String) : ImageAction
    data class UpdateQuality(val quality: Float) : ImageAction
    data object ToggleGrayScale : ImageAction
    data object ToggleSanitize : ImageAction
    data class ChangeFormat(val format: ImageFormat) : ImageAction
    data class CreateFolder(val folderName: String) : ImageAction
    data class LoadImage(val image: Uri): ImageAction

    // Acciones de Ejecución (Botones de Acción)
    data object ProcessPreview : ImageAction // Para refrescar copia en memoria
    data object Save : ImageAction
    data object SaveAndOpen : ImageAction
    data object  Share : ImageAction

    data object ToogleSliceMode : ImageAction
}