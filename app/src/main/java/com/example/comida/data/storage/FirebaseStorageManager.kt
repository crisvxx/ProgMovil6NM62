package com.example.comida.data.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirebaseStorageManager {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    // Subir foto de RECETA (Carpeta 'recipes')
    suspend fun uploadRecipeImage(imageUri: Uri): String {
        return try {
            val fileName = "recipes/${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(fileName)
            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun uploadUserImage(imageUri: Uri, userId: String): String {
        return try {
            // Usamos el ID del usuario como nombre para sobrescribir la anterior si la cambia
            val fileName = "profile_images/$userId.jpg"
            val imageRef = storageRef.child(fileName)
            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
