package com.example.comida.data.firestore

import java.util.Date

data class Review(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val comment: String = "",
    val rating: Int = 0, // 1 a 5 estrellas
    val timestamp: Date = Date() // Para ordenar por fecha
)