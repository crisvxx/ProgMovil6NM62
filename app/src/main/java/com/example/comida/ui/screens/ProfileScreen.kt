package com.example.comida.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comida.viewmodel.AuthViewModel
import com.example.comida.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onMyRecipesClick: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    // Obtenemos ViewModel de Auth para ver datos reales
    val authViewModel: AuthViewModel = viewModel()

    val colors = MaterialTheme.colorScheme
    val scrollState = rememberScrollState() // Para que baje en horizontal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Perfil", style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(30.dp))

        // Avatar
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = colors.primary, modifier = Modifier.size(50.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(authViewModel.currentUserName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.secondary)
        Text(authViewModel.currentUserEmail, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        Text("Personalizar Color de la App", modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp), fontWeight = FontWeight.SemiBold, color = colors.secondary)

        RainbowColorPicker(
            currentColor = themeViewModel.primaryColor,
            onColorChanged = { newColor -> themeViewModel.updateTheme(newColor) }
        )

        TextButton(onClick = { themeViewModel.resetTheme() }) {
            Text("Restablecer color original", color = colors.secondary, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        ProfileOptionButton("Mis Recetas", onClick = onMyRecipesClick, color = colors.secondary)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileOptionButton("Configuración de cuenta", onClick = {}, color = colors.secondary)

        Spacer(modifier = Modifier.height(40.dp)) // Espacio flexible o fijo

        Button(
            onClick = {
                authViewModel.logout()
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cerrar sesión")
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun RainbowColorPicker(currentColor: Color, onColorChanged: (Color) -> Unit) {
    val rainbowColors = listOf(Color.Red, Color(0xFFFF7F00), Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color(0xFF8B00FF), Color.Red)
    var sliderValue by remember { mutableStateOf(0.5f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(currentColor).border(2.dp, Color.Gray, CircleShape))
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(30.dp)) {
            Spacer(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp)).background(brush = Brush.horizontalGradient(rainbowColors)))
            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    val color = interpolateColor(rainbowColors, newValue)
                    onColorChanged(color)
                },
                modifier = Modifier.fillMaxSize(),
                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.Transparent, inactiveTrackColor = Color.Transparent)
            )
        }
    }
}

fun interpolateColor(colors: List<Color>, fraction: Float): Color {
    val sections = colors.size - 1
    val sectionIndex = (fraction * sections).toInt().coerceIn(0, sections - 1)
    val sectionFraction = (fraction * sections) % 1f
    val startColor = colors[sectionIndex]
    val endColor = colors[sectionIndex + 1]
    return Color(
        red = startColor.red + (endColor.red - startColor.red) * sectionFraction,
        green = startColor.green + (endColor.green - startColor.green) * sectionFraction,
        blue = startColor.blue + (endColor.blue - startColor.blue) * sectionFraction,
        alpha = 1f
    )
}

@Composable
fun ProfileOptionButton(text: String, onClick: () -> Unit, color: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = color)
        }
    }
}