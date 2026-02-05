package es.bgaleralop.etereum.presentation.screens.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.bgaleralop.etereum.data.repository.SettingsRepository
import es.bgaleralop.etereum.domain.config.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    val uiState: StateFlow<UserSettings> = repository.settingsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserSettings(
            preferSliderMode = false,
            lastMissionFolder = "",
            deleteOriginal = false
        )
    )

    fun onPreferSliderModeChange(preferSliderMode: Boolean) {
        viewModelScope.launch {
            repository.updatePreferSliderMode(preferSliderMode)
        }
    }

    fun onLastMissionFolderChange(folder: String) {
        viewModelScope.launch {
            repository.updateLastMissionFolder(folder)
        }
    }

    fun onDeleteOriginalChange(deleteOriginal: Boolean) {
        viewModelScope.launch {
            repository.updateDeleteOriginal(deleteOriginal)
        }
    }
}