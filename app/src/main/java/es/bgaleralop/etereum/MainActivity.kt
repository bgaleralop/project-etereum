package es.bgaleralop.etereum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import es.bgaleralop.etereum.presentation.common.UiEvent
import es.bgaleralop.etereum.presentation.screens.imageEdition.EditScreen
import es.bgaleralop.etereum.presentation.screens.imageEdition.EditorViewModel
import es.bgaleralop.etereum.presentation.theme.EtereumTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: EditorViewModel by viewModels()
    private val TAG = "ETEREUM MainActivity: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // INICIAMOS EL LISTENER DE UIEVENT
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is UiEvent.Error -> {
                            Toast.makeText(this@MainActivity, "ALERTA: ${event.message}", Toast.LENGTH_SHORT).show()
                        }
                        is UiEvent.OpenLocation -> {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(event.uri, "image/*")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            try {
                                val chooser = Intent.createChooser(intent, "Abrir Inteligencia")
                                startActivity(chooser)
                            }catch (e: Exception) {
                                Toast.makeText(this@MainActivity, "Error al abrir imagen", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is UiEvent.ShowToast -> {
                            Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // HACEMOS QUE LA APP OCUPE TODA LA PANTALLA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val state = viewModel.state

            EtereumTheme {
                Log.i(TAG, "Componiendo pantalla principal...")
                Scaffold { innerPadding ->
                    EditScreen(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

