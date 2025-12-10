package com.example.comida.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

// Función para mezclar el color elegido con blanco (para fondos suaves)
fun Color.lighten(factor: Float = 0.9f): Color {
    val argb = this.toArgb()
    // Mezcla el color actual con blanco (White)
    val blended = ColorUtils.blendARGB(argb, android.graphics.Color.WHITE, factor)
    return Color(blended)
}

// Función para oscurecer un poco (para textos o bordes)
fun Color.darken(factor: Float = 0.1f): Color {
    val argb = this.toArgb()
    val blended = ColorUtils.blendARGB(argb, android.graphics.Color.BLACK, factor)
    return Color(blended)
}