package es.bgaleralop.etereum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.bgaleralop.etereum.presentation.common.UiEvent
import es.bgaleralop.etereum.presentation.common.components.EtereumTopBar
import es.bgaleralop.etereum.presentation.navigation.Screen
import es.bgaleralop.etereum.presentation.screens.dashboard.LobbyScreen
import es.bgaleralop.etereum.presentation.screens.imageEdition.EditScreen
import es.bgaleralop.etereum.presentation.screens.imageEdition.EditorViewModel
import es.bgaleralop.etereum.presentation.theme.EtereumTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val editorViewModel: EditorViewModel by viewModels()
    private val TAG = "ETEREUM MainActivity: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // HACEMOS QUE LA APP OCUPE TODA LA PANTALLA
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val state = editorViewModel.state

            EtereumTheme {
                Log.i(TAG, "Componiendo pantalla principal...")

                // El NavController actúa como orquestador
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // INICIAMOS EL LISTENER DE UIEVENT
                HandleUiEvents(editorViewModel)

                Scaffold(
                    topBar = {
                        EtereumTopBar(
                            //Solo mostramos la fecha si no estamos en el lobby
                            canNavigateBack = currentRoute != Screen.Lobby::class.qualifiedName,
                            onBackClick = { navController.popBackStack() },
                            onSettingsClick = { }
                        )
                    }
                ) { innerPadding ->
                    // El NavHost gestiona qué pantalla se ve
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Lobby,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable<Screen.Lobby> {
                            LobbyScreen(
                                onNavigateToImages = { navController.navigate(Screen.ImageOps)},
                                onNavigateToDocs = { }
                            )
                        }

                        composable<Screen.ImageOps> {
                            EditScreen(
                                state = state,
                                onAction = editorViewModel::onAction,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HandleUiEvents(viewModel: EditorViewModel){
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Error -> {
                        Toast.makeText(context, "ALERTA: ${event.message}", Toast.LENGTH_SHORT).show()
                    }
                    is UiEvent.OpenLocation -> {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(event.uri, "image/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        try {
                            val chooser = Intent.createChooser(intent, "Abrir Inteligencia")
                            context.startActivity(chooser)
                        }catch (e: Exception) {
                            Toast.makeText(context, "Error al abrir imagen", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is UiEvent.ShowToast -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

