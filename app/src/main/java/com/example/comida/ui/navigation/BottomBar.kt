package com.example.comida.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val colors = MaterialTheme.colorScheme // Accedemos a los colores dinámicos

    NavigationBar(

        containerColor = colors.surface,
        contentColor = colors.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Definimos los items del menú
        val items = listOf(
            Triple(AppRoutes.HOME, "Inicio", Icons.Default.Home),
            Triple(AppRoutes.FAVORITES, "Favoritos", Icons.Default.Favorite),
            Triple(AppRoutes.PROFILE, "Perfil", Icons.Default.Person)
        )

        items.forEach { (route, title, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title) },
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary, // Icono seleccionado (Color fuerte)
                    selectedTextColor = colors.primary,
                    indicatorColor = colors.primary.copy(alpha = 0.2f), // La burbuja de fondo suave
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}