package com.example.comida.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.data.storage.FirebaseStorageManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUserName: String
        get() = auth.currentUser?.displayName ?: "Usuario"

    val currentUserEmail: String
        get() = auth.currentUser?.email ?: ""


    val currentUserPhotoUrl: String?
        get() = auth.currentUser?.photoUrl?.toString()

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onError("Llena todos los campos")
            return
        }
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                onSuccess()
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, pass: String, confirmPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (name.isBlank() || pass.isBlank()) { onError("Llena todos los campos"); return }
        if (pass != confirmPass) { onError("Las contraseñas no coinciden"); return }

        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                val user = result.user
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                user?.updateProfile(profileUpdates)?.await()
                onSuccess()
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }


    fun updateProfilePhoto(imageUri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val url = FirebaseStorageManager.uploadUserImage(imageUri, user.uid)
                if (url.isNotEmpty()) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(url))
                        .build()
                    user.updateProfile(profileUpdates).await()
                    onSuccess()
                } else {
                    onError("Error al subir imagen")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}