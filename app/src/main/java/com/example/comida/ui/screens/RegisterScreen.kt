package com.example.comida.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comida.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterDone: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    // Estado para mostrar errores en rojo
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background) // Fondo dinámico
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(30.dp))

        // CAMPO NOMBRE
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary, focusedLabelColor = colors.primary,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
            )
        )
        Spacer(Modifier.height(10.dp))

        // CAMPO CORREO
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary, focusedLabelColor = colors.primary,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
            )
        )
        Spacer(Modifier.height(10.dp))

        // CAMPO CONTRASEÑA
        OutlinedTextField(
            value = pass, onValueChange = { pass = it },
            label = { Text("Contraseña (mín 8 carácteres)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary, focusedLabelColor = colors.primary,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
            )
        )
        Spacer(Modifier.height(10.dp))

        // CAMPO CONFIRMAR
        OutlinedTextField(
            value = confirmPass, onValueChange = { confirmPass = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary, focusedLabelColor = colors.primary,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
            )
        )

        // MENSAJE DE ERROR
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                authViewModel.register(name, email, pass, confirmPass,
                    onSuccess = {
                        Toast.makeText(context, "¡Cuenta creada! Bienvenido $name", Toast.LENGTH_SHORT).show()
                        onRegisterDone()
                    },
                    onError = { msg -> errorMessage = msg }
                )
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            Text("Registrarme", fontWeight = FontWeight.Bold)
        }
    }
}
