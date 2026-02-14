package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.bgaleralop.etereum.data.repository.SettingsRepository
import es.bgaleralop.etereum.domain.common.Status
import es.bgaleralop.etereum.domain.common.getFileNameFromUri
import es.bgaleralop.etereum.domain.config.UserSettings
import es.bgaleralop.etereum.domain.images.usecases.OpenImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.SaveImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.SaveParams
import es.bgaleralop.etereum.domain.images.usecases.TransformImageUseCase
import es.bgaleralop.etereum.presentation.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val saveImageUseCase: SaveImageUseCase,
    private val transformer: TransformImageUseCase,
    private val openImageUseCase: OpenImageUseCase,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val TAG = "ETEREUM EditorViewModel: "

    var state by mutableStateOf(ImageEditState())
        private set

    // Canal para enviar eventos únicos a la UI
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val userSettings = settingsRepository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UserSettings(false, "", false)
    )

    init {
        viewModelScope.launch {
            userSettings.collect { settings ->
                state = state.copy(
                    isForcedSlider = settings.preferSliderMode,
                    targetDirectory = settings.lastMissionFolder
                )
            }
        }
    }

    fun onAction(action: ImageAction) {
        when (action) {
            is ImageAction.ChangeFormat -> {
                Log.i(TAG, "Cambiando formato de imagen...")
                state = state.copy(
                    targetFormat = action.format,
                    imageStatus = Status.PROCESSING
                )
                processInRealTime()
            }
            is ImageAction.CreateFolder -> {
                Log.i(TAG, "Creando nuevo directorio de misiones...")
                state = state.copy( targetDirectory = action.folderName )
                viewModelScope.launch {
                    settingsRepository.updateLastMissionFolder(action.folderName)
                }
            }
            is ImageAction.LoadImage -> {
                Log.i(TAG, "Cargando imagen desde ${action.image}")
                viewModelScope.launch {
                    val image = openImageUseCase(context, action.image)
                    image.onSuccess {
                        state = state.copy(
                            originalBitmap = it,
                            modifiedBitmap = it,
                            imageStatus = Status.IDLE,
                            outputName = "ETEREUM-${getFileNameFromUri(context, action.image)}",
                        )
                    }.onFailure {
                        Log.e(TAG, "Error al cargar imagen: ${it.message}")
                        _uiEvent.send(UiEvent.Error(it.message ?: "Error al carar imagen"))
                    }

                    Log.d(TAG, "Imagen cargada: ${state.originalBitmap}")
                    processInRealTime()
                }

            }
            ImageAction.ProcessPreview -> {
                Log.i(TAG, "Procesando imagen en tiempo real...")
                state = state.copy(imageStatus = Status.PROCESSING)
                processInRealTime()
            }
            ImageAction.Save -> {
                Log.i(TAG, "Guardando imagen...")
                state = state.copy(imageStatus = Status.PROCESSING)
                saveImage(openAfter = false)
                state = state.copy(imageStatus = Status.COMPLETED)
            }
            ImageAction.SaveAndOpen -> {
                state = state.copy(imageStatus = Status.PROCESSING)
                Log.i(TAG, "Guardando y abriendo imagen...")
                saveImage(openAfter = true)
                state = state.copy(imageStatus = Status.COMPLETED)
            }
            ImageAction.Share -> {
                Log.i(TAG, "Compartiendo imagen...")
            }
            ImageAction.ToggleGrayScale -> {
                Log.i(TAG, "Cambiando escala de grises...")
                state = state.copy(
                    isGrayScale = !state.isGrayScale,
                )
                processInRealTime()
            }
            ImageAction.ToggleSanitize -> {
                if(state.shouldSanitize) {
                    Log.i(TAG, "Sanitizando imagen...")
                } else {
                    Log.i(TAG, "No sanitizando imagen...")
                }
                state = state.copy(
                    shouldSanitize = !state.shouldSanitize,
                )
                processInRealTime()
            }
            is ImageAction.UpdateName -> {
                Log.i(TAG, "Actualizando nombre de archivo...")
                state = state.copy(outputName = action.newName)
            }
            is ImageAction.UpdateQuality -> {
                Log.i(TAG, "Actualizando calidad de imagen...")
                state = state.copy(quality = action.quality, imageStatus = Status.PROCESSING)
                processInRealTime()
            }
            ImageAction.ToogleSliceMode -> {
                state = state.copy(isForcedSlider = !state.isForcedSlider)
            }
            ImageAction.OpenCroopTool -> {
                Log.i(TAG, "Abriendo editor de imágenes...")
            }
        }
    }

    private fun processInRealTime() {
        if (state.originalBitmap != null) {
            Log.d(TAG, "Procesando imagen.")
            state = state.copy(imageStatus = Status.PROCESSING)
            Log.d(TAG, "original bitmap: ${state.originalBitmap}")
            viewModelScope.launch(Dispatchers.Default) {
                //Aquí se genera el modifiedBitmap
                val result = transformer.compressImage(
                    bitmap = state.originalBitmap!!.image,
                    quality = (state.quality * 100).toInt(),
                    isGrayScale = state.isGrayScale
                )

                result.onSuccess {
                    val weight = if(state.originalBitmap!!.weightInBytes <= it.weightInBytes) {
                        state.originalBitmap!!.weightInBytes
                    } else {
                        it.weightInBytes
                    }
                    state = state.copy(
                        modifiedBitmap = it.copy(weightInBytes = weight),
                        imageStatus = Status.COMPLETED)
                    Log.d(TAG, "Imagen procesada correctamente")
                    calculateSavingPercentage()
                }.onFailure {
                    Log.e(TAG, "Error al procesar imagen: ${it.message}")
                    _uiEvent.send(UiEvent.Error(it.message ?: "Error al procesar imagen"))
                    state = state.copy(imageStatus = Status.ERROR)
                }
            }
        }
    }

    private fun saveImage(openAfter: Boolean) {
        // 1. Verificamos que hay algo que guardar.
        val bitmapToSave = state.modifiedBitmap
        if (bitmapToSave == null) {
            Log.e(TAG, "No hay imagen para guardar")
            viewModelScope.launch { _uiEvent.send(UiEvent.Error("No hay imagen")) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            // 2. Preparación de parámetros de la imagen
            val params = SaveParams(
                fileName = state.outputName,
                folder = userSettings.value.lastMissionFolder,
                format = state.targetFormat,
                sanitized = state.shouldSanitize,
                quality = (state.quality * 100).toInt()
            )

            // 3. Ejecución de hilo de I/0
            val result = saveImageUseCase.invoke(bitmap = bitmapToSave.image, params = params)

            // 4. Gestión del resultado.
            result.onSuccess {
                if (openAfter) {
                    Log.i(TAG, "Abriendo imagen guardada")
                    viewModelScope.launch { _uiEvent.send(UiEvent.OpenLocation(it)) }
                } else {
                    Log.i(TAG, "Imagen guardada en ${it.path}")
                    viewModelScope.launch { _uiEvent.send(UiEvent.ShowToast("Imagen guardada")) }
                }
            }.onFailure { error ->
                Log.e(TAG, "Error al guardar imagen: ${error.message}")
                _uiEvent.send(UiEvent.Error("Fallo en guardado: ${error.message}"))
            }
        }
    }

    private fun calculateSavingPercentage() {
        val originalSize = state.originalBitmap?.weightInBytes ?: 0
        val currentSize = state.modifiedBitmap?.weightInBytes ?: 0
        val savingPercentage = ((originalSize - currentSize) * 100 / originalSize).toInt()

        state = state.copy(savingPercentage = if(savingPercentage > 0 ) savingPercentage else 0)
    }
}