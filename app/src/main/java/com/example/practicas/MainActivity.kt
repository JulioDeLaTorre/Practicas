package com.example.practicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()//commit
        }
    }
}
@Composable
fun MainScreen() {
    var salario by remember { mutableStateOf("") }
    var isr by remember { mutableStateOf("") }
    var neto by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.satmalevolo),contentDescription = "satmalevoloxd",modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Texto(text = "Sueldo quincenal:",colorLetra = Color.White,fondo = Color.Blue)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            TextField(
                value = salario,
                onValueChange = { it -> salario = it },
                label = { Text("Ingresa sueldo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                val sueldo = salario.toDouble()
                var A = 0.0
                var B = 0.0

                if (sueldo <= 368.10) {
                    A = ((sueldo - 0.01) * 0.0192) + 0.00
                } else if (sueldo <= 3124.35) {
                    A = ((sueldo - 368.11) * 0.0640) + 7.05
                } else if (sueldo <= 5490.75) {
                    A = ((sueldo - 3124.36) * 0.1088) + 183.45
                } else if (sueldo <= 6382.80) {
                    A = ((sueldo - 5490.76) * 0.16) + 441.00
                } else if (sueldo <= 7641.90) {
                    A = ((sueldo - 6382.81) * 0.1792) + 583.65
                } else if (sueldo <= 15412.80) {
                    A = ((sueldo - 7641.91) * 0.2136) + 809.25
                } else if (sueldo <= 24292.65) {
                    A = ((sueldo - 15412.81) * 0.2352) + 2469.15
                } else if (sueldo <= 46378.50) {
                    A = ((sueldo - 24292.66) * 0.30) + 4557.75
                } else if (sueldo <= 61838.10) {
                    A = ((sueldo - 46378.51) * 0.32) + 11183.40
                } else if (sueldo <= 185514.30) {
                    A = ((sueldo - 61838.11) * 0.34) + 16130.55
                } else if (sueldo >= 185514.31) {
                    A = ((sueldo - 185514.31) * 0.35) + 58180.35
                }
                B = sueldo - A
                isr = A.toString()
                neto = B.toString()
            }) {
                Text("Calcular")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Texto("ISR: ",Color.Black,Color.LightGray)
            Texto(isr,Color.Black,Color.LightGray)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Texto("Neto: ",Color.Black,Color.Green)
            Texto(neto,Color.Black,Color.Green)
        }
    }
}

@Composable
fun Texto(text: String, colorLetra: Color, fondo: Color) {
    Text(
        text = text,
        color = colorLetra,
        modifier = Modifier
            .background(fondo)
            .padding(4.dp)
    )
}




