package com.example.comida.data.firestore
import com.google.firebase.firestore.DocumentId

data class Recipe(
    @DocumentId
    val id: String = "",
    val titulo: String = "",
    val ingredientes: List<String> = emptyList(),
    val instrucciones: String = "",
    val imagenUrl: String = "",
    val userId: String = "",
    val categoria: String = "Todas",
    val likes: List<String> = emptyList()
)