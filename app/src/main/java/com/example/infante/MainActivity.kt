package com.example.infante

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


//LIBRERIAS PARA LA PARTE DEL COLOR
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UIPrincipal()
        }
    }
}

data class Animal(
    val imagen: Int,
    val sonido: Int,
    val nombre: String
)

@Composable
fun BotonAnimal(imgRes: Int, sonidoRes: Int, nombre: String){
    val contexto = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)) // 1. Sombra suave
            .size(width = 140.dp, height = 150.dp) // 2. Hacemos el botón un poco más grande
            .clip(RoundedCornerShape(16.dp)) // 3. Esquinas redondeadas
            .background(MaterialTheme.colorScheme.surface) // 4. Fondo blanco (de nuestro tema)
            .clickable {
                MediaPlayer
                    .create(contexto, sonidoRes)
                    .start()
            }
            .padding(8.dp) // Espacio interno
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = nombre,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre imagen y texto
        Text(
            text = nombre,
            // 5. Texto más grande, en negrita y con el color del tema
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
@Composable
fun UIPrincipal(){
    // La lista de animales está perfecta
    val animales = listOf(
        Animal(R.drawable.perro, R.raw.sonido_perro, "Perro"),
        Animal(R.drawable.gato, R.raw.sonido_gato, "Gato"),
        Animal(R.drawable.vaca, R.raw.sonido_vaca, "Vaca"),
        Animal(R.drawable.leon, R.raw.sonido_leon, "León"),
        Animal(R.drawable.pato, R.raw.sonido_pato, "Pato"),
        Animal(R.drawable.mono, R.raw.sonido_mono, "Mono")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 1. Aplicamos el color de fondo de nuestro tema
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp), // Un poco de espacio en los bordes
        horizontalAlignment = Alignment.CenterHorizontally,
        // 2. Cambiamos el arrangement para un mejor espaciado
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a mi mundo de animales",
            // 3. Aplicamos el color primario (azul) y un estilo más grande
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Toca un animal",
            // 4. Aplicamos el color secundario (naranja)
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )


        // Primera fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            BotonAnimal(
                imgRes = animales[0].imagen,
                sonidoRes = animales[0].sonido,
                nombre = animales[0].nombre
            )
            BotonAnimal(
                imgRes = animales[1].imagen,
                sonidoRes = animales[1].sonido,
                nombre = animales[1].nombre
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        // Segunda fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BotonAnimal(
                imgRes = animales[2].imagen,
                sonidoRes = animales[2].sonido,
                nombre = animales[2].nombre
            )
            BotonAnimal(
                imgRes = animales[3].imagen,
                sonidoRes = animales[3].sonido,
                nombre = animales[3].nombre
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        // Tercera fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BotonAnimal(
                imgRes = animales[4].imagen,
                sonidoRes = animales[4].sonido,
                nombre = animales[4].nombre
            )
            BotonAnimal(
                imgRes = animales[5].imagen,
                sonidoRes = animales[5].sonido,
                nombre = animales[5].nombre
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UIPrincipal()
}