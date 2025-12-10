package com.example.comida.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestaurantMenu // Icono para vacío
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.comida.data.firestore.Recipe
import com.example.comida.ui.components.EmptyState
import com.example.comida.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyRecipesScreen(onBack: () -> Unit) {
    val recipeViewModel: RecipeViewModel = viewModel()
    val allRecipes by recipeViewModel.recipes.collectAsState()
    val colors = MaterialTheme.colorScheme

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val myRecipes = allRecipes.filter { it.userId == currentUserId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
    ) {
        Text("Mis Recetas", style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        if (currentUserId == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Inicia sesión para ver tus recetas", color = Color.Gray)
            }
        } else if (myRecipes.isEmpty()) {

            EmptyState(
                message = "No has creado ninguna receta.\n¡Comparte tu talento culinario!",
                icon = Icons.Default.RestaurantMenu
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(myRecipes) { recipe ->
                    MyRecipeItem(
                        recipe = recipe,
                        onDelete = { recipeViewModel.deleteRecipe(recipe.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun MyRecipeItem(recipe: Recipe, onDelete: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = recipe.imagenUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp).background(Color.LightGray)
            )
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(recipe.titulo, fontWeight = FontWeight.Bold, color = colors.secondary, maxLines = 1)
                Text(recipe.categoria, style = MaterialTheme.typography.bodySmall, color = colors.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
            }
        }
    }
}