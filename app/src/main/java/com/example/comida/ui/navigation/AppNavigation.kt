package com.example.comida.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.example.comida.ui.screens.*
import com.example.comida.viewmodel.RecipeViewModel
import com.example.comida.viewmodel.ThemeViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    themeViewModel: ThemeViewModel
) {
    // Detectamos en qué ruta estamos
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(

        bottomBar = {
            if (currentRoute !in listOf(AppRoutes.SPLASH, AppRoutes.LOGIN, AppRoutes.REGISTER, AppRoutes.DETAIL)) {
                BottomBar(navController)
            }
        },

        floatingActionButton = {
            // Solo mostramos el botón si estamos en el HOME
            if (currentRoute == AppRoutes.HOME) {
                FloatingActionButton(
                    onClick = { navController.navigate(AppRoutes.ADD_RECIPE) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Receta")
                }
            }
        }
    ) { padding ->

        AnimatedNavHost(
            navController = navController,
            startDestination = AppRoutes.SPLASH,
            modifier = Modifier.padding(padding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut() }
        ) {



            composable(AppRoutes.SPLASH) {
                SplashScreen { navController.navigate(AppRoutes.LOGIN) { popUpTo(0) } }
            }
            composable(AppRoutes.LOGIN) {
                LoginScreen(
                    onLoginClick = { navController.navigate(AppRoutes.HOME) },
                    onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
                )
            }
            composable(AppRoutes.REGISTER) {
                RegisterScreen { navController.navigate(AppRoutes.HOME) }
            }

            // ⭐ HOME (Ya no necesita lógica de botón interna)
            composable(AppRoutes.HOME) {
                HomeScreen(
                    onRecipeClick = { recipeId -> navController.navigate("detail/$recipeId") }
                    // Ya no pasamos onAddClick aquí, lo maneja el Scaffold de arriba
                )
            }

            composable(AppRoutes.ADD_RECIPE) { AddRecipeScreen() }
            composable(AppRoutes.FAVORITES) { FavoritesScreen() }
            composable(AppRoutes.MY_RECIPES) { MyRecipesScreen { navController.popBackStack() } }

            composable(AppRoutes.PROFILE) {
                ProfileScreen(
                    onLogout = { navController.navigate(AppRoutes.LOGIN) { popUpTo(0) } },
                    onMyRecipesClick = { navController.navigate(AppRoutes.MY_RECIPES) },
                    themeViewModel = themeViewModel
                )
            }

            composable(
                route = AppRoutes.DETAIL,
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("recipeId")
                if (id != null) {
                    RecipeDetailScreen(recipeId = id, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}