package com.example.comida.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object RecipeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val recipesRef = db.collection("recipes")

    // --- RECETAS
    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val newDoc = recipesRef.document()
        val recipeWithId = recipe.copy(id = newDoc.id)
        newDoc.set(recipeWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getRecipes(onData: (List<Recipe>) -> Unit) {
        recipesRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                onData(snapshot.toObjects(Recipe::class.java))
            }
        }
    }

    fun deleteRecipe(recipeId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        recipesRef.document(recipeId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }


    // Agregar un comentario dentro de una receta
    fun addReview(recipeId: String, review: Review, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val reviewRef = recipesRef.document(recipeId).collection("reviews").document()
        val reviewWithId = review.copy(id = reviewRef.id)

        reviewRef.set(reviewWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // Escuchar comentarios de una receta específica en tiempo real
    fun getReviewsForRecipe(recipeId: String, onData: (List<Review>) -> Unit) {
        recipesRef.document(recipeId).collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Los más nuevos primero
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    onData(snapshot.toObjects(Review::class.java))
                }
            }
    }
}