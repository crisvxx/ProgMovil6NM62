package com.example.comida.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.comida.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth

val selectionCategories = listOf("Desayuno", "Almuerzo", "Cena", "Postre")

@Composable
fun AddRecipeScreen() {
    val recipeViewModel: RecipeViewModel = viewModel()
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    // Estados del formulario
    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Almuerzo") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    // Estado de carga para evitar doble clic
    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImage = uri
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nueva Receta", style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        // Selector de Imagen
        Box(
            modifier = Modifier
                .fillMaxWidth().height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, colors.surface, RoundedCornerShape(16.dp))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                AsyncImage(model = selectedImage, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                    Text("Toca para subir foto", color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de Categoría
        Text("Selecciona una categoría:", color = colors.secondary, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectionCategories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.primary,
                        selectedLabelColor = Color.White,
                        containerColor = colors.surface,
                        labelColor = colors.secondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MealInputText(value = title, onValueChange = { title = it }, label = "Nombre del platillo")
        Spacer(modifier = Modifier.height(16.dp))
        MealInputText(value = ingredients, onValueChange = { ingredients = it }, label = "Ingredientes (separados por comas)", singleLine = false)
        Spacer(modifier = Modifier.height(16.dp))
        MealInputText(value = instructions, onValueChange = { instructions = it }, label = "Instrucciones", singleLine = false, minLines = 5)

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÓN GUARDAR ACTUALIZADO
        Button(
            onClick = {
                // 1. Obtenemos el ID del usuario actual
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                if (title.isNotEmpty() && currentUserId != null) {
                    isLoading = true // Bloqueamos botón

                    // 2. Llamamos a la función con TODOS los parámetros nuevos
                    recipeViewModel.uploadRecipe(
                        titulo = title,
                        ingredientes = ingredients.split(",").map { it.trim() },
                        instrucciones = instructions,
                        imageUri = selectedImage,
                        categoria = selectedCategory,
                        userId = currentUserId, // ⭐ AQUÍ PASAMOS EL ID
                        onSuccess = {
                            isLoading = false
                            Toast.makeText(context, "✅ ¡Receta guardada con éxito!", Toast.LENGTH_LONG).show()

                            // Limpiamos campos
                            title = ""
                            ingredients = ""
                            instructions = ""
                            selectedImage = null
                        },
                        onError = { errorMsg ->
                            isLoading = false
                            Toast.makeText(context, "❌ Error: $errorMsg", Toast.LENGTH_LONG).show()
                        }
                    )
                } else if (currentUserId == null) {
                    Toast.makeText(context, "Error: No has iniciado sesión", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Por favor escribe un título", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading, // Deshabilitar si está cargando
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publicar Receta")
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun MealInputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = colors.primary,
            cursorColor = colors.primary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )
}