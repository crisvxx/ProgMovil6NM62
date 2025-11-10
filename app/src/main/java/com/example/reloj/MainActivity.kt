package com.example.reloj

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                AppReloj()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AppReloj() {
        var iniciado by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = !iniciado,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PantallaInicio(onIniciar = { iniciado = true })
                }

                AnimatedVisibility(
                    visible = iniciado,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PantallaReloj()
                }
            }
        }
    }

    @Composable
    fun PantallaInicio(onIniciar: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RELOJ INTELIGENTE",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = onIniciar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD600),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(55.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
            ) {
                Text("INICIAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun PantallaReloj() {
        var horaActual by remember { mutableStateOf(LocalTime.now()) }
        var botonHabilitado by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            while (true) {
                horaActual = LocalTime.now()
                delay(1000)
            }
        }

        val hora = horaActual.format(DateTimeFormatter.ofPattern("hh:mm"))
        val ampm = horaActual.format(DateTimeFormatter.ofPattern("a"))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RELOJ INTELIGENTE",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFD600),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = hora,
                    fontSize = 90.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ampm,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFFFFD600)
                )
            }

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = {
                    botonHabilitado = false
                    reproducirHora(horaActual) {
                        botonHabilitado = true
                    }
                },
                enabled = botonHabilitado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD600),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(230.dp)
                    .height(55.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
            ) {
                Text("La hora es...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun reproducirHora(hora: LocalTime, onFinish: () -> Unit) {
        val listaAudios = mutableListOf<Int>()
        val hora12 = if (hora.hour % 12 == 0) 12 else hora.hour % 12
        val minutos = hora.minute
        val esPM = hora.hour >= 12

        if (hora12 == 1) listaAudios.add(R.raw.son_la)
        else listaAudios.add(R.raw.son_las)

        listaAudios.add(obtenerAudioNumero(hora12))
        if (minutos > 0) listaAudios.add(obtenerAudioNumero(minutos))
        listaAudios.add(if (esPM) R.raw.pm else R.raw.am)

        reproducirSecuencia(listaAudios, onFinish)
    }

    private fun obtenerAudioNumero(num: Int): Int {
        return when (num) {
            1 -> R.raw.uno
            2 -> R.raw.dos
            3 -> R.raw.tres
            4 -> R.raw.cuatro
            5 -> R.raw.cinco
            6 -> R.raw.seis
            7 -> R.raw.siete
            8 -> R.raw.ocho
            9 -> R.raw.nueve
            10 -> R.raw.diez
            11 -> R.raw.once
            12 -> R.raw.doce
            else -> {
                val nombre = "n$num"
                resources.getIdentifier(nombre, "raw", packageName)
            }
        }
    }

    private fun reproducirSecuencia(audios: List<Int>, onFinish: () -> Unit) {
        if (audios.isEmpty()) {
            onFinish()
            return
        }
        reproducirSecuenciaRecursiva(audios, 0, onFinish)
    }

    private fun reproducirSecuenciaRecursiva(audios: List<Int>, indice: Int, onFinish: () -> Unit) {
        if (indice >= audios.size) {
            onFinish()
            return
        }

        val mp = MediaPlayer.create(this, audios[indice])
        mp.setOnCompletionListener {
            mp.release()
            reproducirSecuenciaRecursiva(audios, indice + 1, onFinish)
        }
        mp.start()
    }
}



