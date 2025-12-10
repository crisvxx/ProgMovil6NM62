package com.example.comida.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ComidaTheme(
    mainColor: Color, // ⭐ ESTO ES LO QUE LE FALTABA, el parámetro para recibir el color
    content: @Composable () -> Unit
) {
    // Funciones auxiliares para aclarar/oscurecer (si no las tienes en ColorUtils, las pongo aquí simple)
    // Si ya creaste ColorUtils.kt, puedes borrar estas dos líneas de 'fun' y usar las del archivo.
    // Si NO creaste ColorUtils.kt, mantén esta lógica simple aquí:

    val primary = mainColor
    val secondary = mainColor
    // Un fondo muy clarito (mezcla con blanco)
    val background = Color(
        red = (mainColor.red * 0.1f + 0.9f),
        green = (mainColor.green * 0.1f + 0.9f),
        blue = (mainColor.blue * 0.1f + 0.9f),
        alpha = 1f
    )

    val colorScheme = lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        secondary = secondary,
        background = background,
        surface = background,
        onBackground = secondary,
        onSurface = secondary,
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}