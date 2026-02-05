package es.bgaleralop.etereum.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable data object Lobby : Screen()
    @Serializable data object ImageOps : Screen()
    @Serializable data object DocOps : Screen()
}