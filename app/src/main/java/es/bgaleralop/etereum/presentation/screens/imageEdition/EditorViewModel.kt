package es.bgaleralop.etereum.presentation.screens.imageEdition

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.bgaleralop.etereum.R
import es.bgaleralop.etereum.domain.common.Status
import es.bgaleralop.etereum.domain.images.model.ImageProcessResult
import es.bgaleralop.etereum.domain.images.services.toRawByteArray
import es.bgaleralop.etereum.domain.images.usecases.OpenImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.SaveImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.SaveParams
import es.bgaleralop.etereum.domain.images.usecases.TransformImageUseCase
import es.bgaleralop.etereum.presentation.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    imageUri: Uri? = null,
    @ApplicationContext private val context: Context,
    private val saveImageUseCase: SaveImageUseCase,
    private val transformer: TransformImageUseCase,
    private val openImageUseCase: OpenImageUseCase,
): ViewModel() {
    private val TAG = "ETEREUM EditorViewModel: "

    var state by mutableStateOf(ImageEditState())
        private set

    // Canal para enviar eventos únicos a la UI
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        Log.d(TAG, "Iniciado...")
        if(imageUri != null) {
            onAction(ImageAction.LoadImage(imageUri))
        } else {
            loadResourceImage(R.drawable.pict1571)
        }
    }

    fun onAction(action: ImageAction) {
        when (action) {
            is ImageAction.ChangeFormat -> {
                Log.i(TAG, "Cambiando formato de imagen...")
                state = state.copy(
                    targetFormat = action.format
                )
                processInRealTime()
            }
            is ImageAction.CreateFolder -> {
                Log.i(TAG, "Creando nuevo directorio de misiones...")
                state = state.copy( targetDirectory = action.folderName )
            }
            is ImageAction.LoadImage -> {
                Log.i(TAG, "Cargando imagen desde ${action.image}")
                viewModelScope.launch {
                    val image = openImageUseCase(action.image)
                    image.onSuccess {
                        state = state.copy(
                            originalBitmap = it,
                            modifiedBitmap = it,
                            imageStatus = Status.IDLE,
                            outputName = "ETEREUM-copy-${System.currentTimeMillis()}",
                        )
                    }.onFailure {
                        Log.e(TAG, "Error al cargar imagen: ${it.message}")
                        _uiEvent.send(UiEvent.Error(it.message ?: "Error al carar imagen"))
                    }

                    Log.d(TAG, "Imagen cargada: ${state.originalBitmap}")
                }

            }
            ImageAction.ProcessPreview -> {
                Log.i(TAG, "Procesando imagen en tiempo real...")
                processInRealTime()
            }
            ImageAction.Save -> {
                Log.i(TAG, "Guardando imagen...")
                saveImage(openAfter = false)
            }
            ImageAction.SaveAndOpen -> {
                Log.i(TAG, "Guardando y abriendo imagen...")
                saveImage(openAfter = true)
            }
            ImageAction.Share -> {
                Log.i(TAG, "Compartiendo imagen...")
            }
            ImageAction.ToggleGrayScale -> {
                Log.i(TAG, "Cambiando escala de grises...")
                state = state.copy(
                    isGrayScale = !state.isGrayScale
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
                    shouldSanitize = !state.shouldSanitize
                )
                processInRealTime()
            }
            is ImageAction.UpdateName -> {
                Log.i(TAG, "Actualizando nombre de archivo...")
                state = state.copy(outputName = action.newName)
            }
            is ImageAction.UpdateQuality -> {
                Log.i(TAG, "Actualizando calidad de imagen...")
                state = state.copy(quality = action.quality)
                processInRealTime()
            }
        }
    }

    private fun processInRealTime() {
        Log.d(TAG, "Procesando imagen en tiempo real...")
        if (state.originalBitmap != null) {

            viewModelScope.launch(Dispatchers.Default) {
                //Aquí se genera el modifiedBitmap
                val result = transformer.compressImage(
                    bitmap = state.originalBitmap!!.image,
                    quality = (state.quality * 100).toInt(),
                    isGrayScale = state.isGrayScale
                )

                result.onSuccess {
                    state = state.copy(modifiedBitmap = it)
                }.onFailure {
                    Log.e(TAG, "Error al procesar imagen: ${it.message}")
                    _uiEvent.send(UiEvent.Error(it.message ?: "Error al procesar imagen"))
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
                folder = state.targetDirectory,
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

    private fun loadResourceImage(resId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeResource(context.resources, resId, options)

            // Calcular el factor de reudccion
            options.inSampleSize = 8

            // Decodificar realmente con el tamaño reducido.
            options.inJustDecodeBounds = false
            val finalBitmap = BitmapFactory.decodeResource(context.resources, resId, options)
            val image = ImageProcessResult(finalBitmap, finalBitmap.toRawByteArray().size, false)
            state = state.copy(
                originalBitmap = image,
                modifiedBitmap = image,
                outputName = "IMG_DEMO"
            )
        }
    }
}