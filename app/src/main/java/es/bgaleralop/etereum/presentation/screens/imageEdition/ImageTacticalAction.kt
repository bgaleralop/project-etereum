package es.bgaleralop.etereum.presentation.screens.imageEdition

import es.bgaleralop.etereum.domain.images.model.ImageFormat

/**
 * Conjunto de acciones posibles que puede ejecutar el usuario.
 */
sealed interface ImageTacticalAction {
    // Acciones de Configuracion (Cambios en el Panel de Control)
    data class UpdateName(val newName: String) : ImageTacticalAction
    data class UpdateQuality(val quality: Float) : ImageTacticalAction
    data object ToggleGrayScale : ImageTacticalAction
    data object ToggleSanitize : ImageTacticalAction
    data class ChangeFormat(val format: ImageFormat) : ImageTacticalAction
    data class CreateFolder(val folderName: String) : ImageTacticalAction
    data class LoadImage(val image: Int): ImageTacticalAction

    // Acciones de Ejecución (Botones de Acción)
    data object ProcessPreview : ImageTacticalAction // Para refrescar copia en memoria
    data object Save : ImageTacticalAction
    data object SaveAndOpen : ImageTacticalAction
    data object  Share : ImageTacticalAction
}