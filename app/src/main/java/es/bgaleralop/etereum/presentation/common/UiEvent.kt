package es.bgaleralop.etereum.presentation.common

import android.net.Uri

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class OpenLocation(val uri: Uri) : UiEvent()
    data class Error(val message: String) : UiEvent()
}