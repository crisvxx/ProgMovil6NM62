package com.example.comida.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("meal_prefs", Context.MODE_PRIVATE)

    // Color por defecto (El Marrón original de MealCraft)
    private val defaultColor = 0xFF8D5A34.toInt()

    // Variable observable que guarda el color actual
    var primaryColor by mutableStateOf(Color(defaultColor))
        private set

    init {
        loadTheme()
    }

    fun updateTheme(color: Color) {
        primaryColor = color
        prefs.edit().putInt("theme_color", color.toArgb()).apply()
    }


    fun resetTheme() {
        val originalColor = Color(defaultColor)
        updateTheme(originalColor)
    }


    private fun loadTheme() {
        val savedColorInt = prefs.getInt("theme_color", defaultColor)
        primaryColor = Color(savedColorInt)
    }
}