package com.example.comida.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FavoriteBorder
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
fun FavoritesScreen() {
    val recipeViewModel: RecipeViewModel = viewModel()
    val allRecipes by recipeViewModel.recipes.collectAsState()
    val colors = MaterialTheme.colorScheme
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Filtrar mis likes
    val favoriteRecipes = allRecipes.filter { it.likes.contains(currentUserId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
    ) {
        Text("Favoritos", style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        if (favoriteRecipes.isEmpty()) {

            EmptyState(
                message = "Aún no tienes favoritos.\n¡Dale amor a algunas recetas!",
                icon = Icons.Default.FavoriteBorder
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(favoriteRecipes) { recipe ->
                    FavoriteItemCard(recipe)
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun FavoriteItemCard(recipe: Recipe) {
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
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                Text(text = recipe.titulo, style = MaterialTheme.typography.titleMedium, color = colors.secondary, fontWeight = FontWeight.Bold, maxLines = 1)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "30 min", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}