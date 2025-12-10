package com.example.comida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.comida.ui.navigation.AppNavigation
import com.example.comida.ui.theme.ComidaTheme
import com.example.comida.viewmodel.RecipeViewModel
import com.example.comida.viewmodel.ThemeViewModel // Importa el nuevo ViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Inicializamos el ViewModel del Tema
            val themeViewModel: ThemeViewModel = viewModel()

            // 2. Aplicamos el tema usando el color guardado (primaryColor)
            ComidaTheme(mainColor = themeViewModel.primaryColor) {

                val navController = rememberAnimatedNavController()
                val recipeViewModel: RecipeViewModel = viewModel()

                // 3. Pasamos el themeViewModel a la navegación
                // (Para que llegue hasta la pantalla de Perfil)
                AppNavigation(
                    navController = navController,
                    recipeViewModel = recipeViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
