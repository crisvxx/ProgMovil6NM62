package com.example.comida.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.data.firestore.Recipe
import com.example.comida.data.firestore.RecipeRepository
import com.example.comida.data.firestore.Review // Importar Review
import com.example.comida.data.storage.FirebaseStorageManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()


    private val _currentReviews = MutableStateFlow<List<Review>>(emptyList())
    val currentReviews: StateFlow<List<Review>> = _currentReviews.asStateFlow()

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        RecipeRepository.getRecipes { listaRecetas ->
            _recipes.value = listaRecetas
        }
    }


    fun fetchReviews(recipeId: String) {
        RecipeRepository.getReviewsForRecipe(recipeId) { reviews ->
            _currentReviews.value = reviews
        }
    }


    fun submitReview(recipeId: String, commentText: String, rating: Int) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        // Creamos el objeto Review
        val review = Review(
            userId = user.uid,
            userName = user.displayName ?: "Anónimo",
            comment = commentText,
            rating = rating
        )

        viewModelScope.launch {
            RecipeRepository.addReview(recipeId, review,
                onSuccess = { /* Comentario guardado */ },
                onError = { e -> e.printStackTrace() }
            )
        }
    }

    // ... (El resto de tus funciones: uploadRecipe, deleteRecipe, toggleFavorite SIGUEN IGUAL) ...

    fun uploadRecipe(
        titulo: String,
        ingredientes: List<String>,
        instrucciones: String,
        imageUri: Uri?,
        categoria: String,
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val imageUrl = if (imageUri != null) {
                    FirebaseStorageManager.uploadRecipeImage(imageUri)
                } else {
                    ""
                }
                val recipe = Recipe(
                    titulo = titulo,
                    ingredientes = ingredientes,
                    instrucciones = instrucciones,
                    imagenUrl = imageUrl,
                    categoria = categoria,
                    userId = userId,
                    likes = emptyList()
                )
                RecipeRepository.addRecipe(recipe,
                    onSuccess = { onSuccess() },
                    onError = { e -> onError(e.message ?: "Error desconocido") }
                )
            } catch (e: Exception) {
                onError(e.message ?: "Error al procesar la imagen")
            }
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            RecipeRepository.deleteRecipe(recipeId, {}, { e -> e.printStackTrace() })
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val isLiked = recipe.likes.contains(currentUserId)

        val newLikes = if (isLiked) {
            recipe.likes - currentUserId
        } else {
            recipe.likes + currentUserId
        }

        FirebaseFirestore.getInstance().collection("recipes").document(recipe.id)
            .update("likes", newLikes)
    }
}