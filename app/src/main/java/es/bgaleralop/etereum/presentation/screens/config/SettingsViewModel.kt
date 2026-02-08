package es.bgaleralop.etereum.presentation.screens.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.bgaleralop.etereum.data.repository.SettingsRepository
import es.bgaleralop.etereum.domain.config.UserSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    var state by mutableStateOf(SettingsUiState())
        private set
    val settings: StateFlow<UserSettings> = repository.settingsFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        UserSettings(
            preferSliderMode = false,
            lastMissionFolder = "",
            deleteOriginal = false
        )
    )

    // Job para evitar ejecutar escrituras por caracter en el datastore
    private var debounceJob: Job? = null

    init {
       viewModelScope.launch {
           val initialSettings = repository.settingsFlow.first()
           state = state.copy(
               preferSliderMode = initialSettings.preferSliderMode,
               lastMissionFolder = initialSettings.lastMissionFolder,
               deleteOriginal = initialSettings.deleteOriginal
           )
       }
    }

    fun onPreferSliderModeChange(preferSliderMode: Boolean) {
        state = state.copy(preferSliderMode = preferSliderMode)
        viewModelScope.launch {
            repository.updatePreferSliderMode(preferSliderMode)
        }
    }

    fun onLastMissionFolderChange(folder: String) {
        state = state.copy(lastMissionFolder = folder)

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(400)
            repository.updateLastMissionFolder(folder)
        }
    }

    fun onDeleteOriginalChange(deleteOriginal: Boolean) {
        state = state.copy(deleteOriginal = deleteOriginal)
        viewModelScope.launch {
            repository.updateDeleteOriginal(deleteOriginal)
        }
    }
}