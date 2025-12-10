package com.example.comida.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.comida.data.firestore.Review
import com.example.comida.ui.theme.* // Asegúrate de tener tus colores aquí o usa MaterialTheme
import com.example.comida.viewmodel.RecipeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onBack: () -> Unit
) {
    val recipeViewModel: RecipeViewModel = viewModel()
    val allRecipes by recipeViewModel.recipes.collectAsState()+
            .
    val reviews by recipeViewModel.currentReviews.collectAsState()

    // Buscamos la receta
    val recipe = allRecipes.find { it.id == recipeId }
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme


    LaunchedEffect(recipeId) {
        recipeViewModel.fetchReviews(recipeId)
    }

    // Estados para el formulario de nuevo comentario
    var userRating by remember { mutableIntStateOf(0) }
    var userComment by remember { mutableStateOf("") }

    if (recipe == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada", color = Color.Gray)
        }
        return
    }

    fun shareRecipe() {
        val shareText = """
            🍽️ ¡Mira esta receta en MealCraft!
            
            *${recipe.titulo}*
            
            📝 Ingredientes:
            ${recipe.ingredientes.joinToString("\n") { "- $it" }}
            
            Descubre más en mi App.
        """.trimIndent()

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartir receta vía...")
        context.startActivity(shareIntent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER CON IMAGEN ---
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AsyncImage(
                model = recipe.imagenUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Botones flotantes (Atrás y Compartir)
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart).clip(CircleShape).background(Color.White.copy(alpha = 0.8f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = colors.primary)
            }

            IconButton(
                onClick = { shareRecipe() },
                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd).clip(CircleShape).background(Color.White.copy(alpha = 0.8f))
            ) {
                Icon(Icons.Default.Share, contentDescription = "Compartir", tint = colors.primary)
            }
        }

        // --- CONTENIDO ---
        Column(
            modifier = Modifier
                .offset(y = (-20).dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(colors.background)
                .padding(24.dp)
        ) {
            Text(recipe.titulo, style = MaterialTheme.typography.headlineMedium, color = colors.primary, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Surface(color = colors.surface, shape = RoundedCornerShape(8.dp)) {
                    Text(recipe.categoria, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = colors.secondary, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Timer, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("30 min", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ingredientes
            Text("Ingredientes", style = MaterialTheme.typography.titleLarge, color = colors.secondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            recipe.ingredientes.forEach { ingrediente ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", color = colors.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(ingrediente, color = colors.secondary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instrucciones
            Text("Instrucciones", style = MaterialTheme.typography.titleLarge, color = colors.secondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = recipe.instrucciones, style = MaterialTheme.typography.bodyLarge, color = colors.secondary, lineHeight = 24.sp)

            Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray)


            Text("Tu Opinión", style = MaterialTheme.typography.titleLarge, color = colors.secondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Califica esta receta:", fontSize = 14.sp, color = Color.Gray)
                    // Estrellas Interactivas
                    InteractiveRatingBar(
                        currentRating = userRating,
                        onRatingChanged = { userRating = it },
                        starColor = Color(0xFFFFC107) // Amarillo Dorado
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = userComment,
                        onValueChange = { userComment = it },
                        placeholder = { Text("Escribe un comentario...") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (userRating > 0 && userComment.isNotBlank()) {
                                recipeViewModel.submitReview(recipe.id, userComment, userRating)
                                Toast.makeText(context, "¡Gracias por tu opinión!", Toast.LENGTH_SHORT).show()
                                // Limpiar
                                userRating = 0
                                userComment = ""
                            } else {
                                Toast.makeText(context, "Elige una calificación y escribe algo", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Enviar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text("Reseñas (${reviews.size})", style = MaterialTheme.typography.titleLarge, color = colors.secondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (reviews.isEmpty()) {
                Text("Sé el primero en opinar sobre este platillo.", color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            } else {
                reviews.forEach { review ->
                    ReviewItem(review)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// COMPONENTE: ESTRELLAS INTERACTIVAS (Para calificar)
@Composable
fun InteractiveRatingBar(
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    maxStars: Int = 5,
    starColor: Color = Color.Yellow
) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (i <= currentRating) starColor else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}

// COMPONENTE: TARJETA DE RESEÑA (Para ver)
@Composable
fun ReviewItem(review: Review) {
    val colors = MaterialTheme.colorScheme
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Nombre del usuario
                Text(review.userName, fontWeight = FontWeight.Bold, color = colors.secondary)
                Spacer(modifier = Modifier.weight(1f))
                // Estrellitas pequeñas estáticas
                repeat(review.rating) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Fecha
            Text(dateFormat.format(review.timestamp), fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            // Comentario
            Text(review.comment, color = Color.DarkGray)
        }
    }
}