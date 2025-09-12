package com.example.practicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicas.ui.theme.PracticasTheme
import java.util.Stack
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticasTheme {
                Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
                  ) {
                MainScreen()
            }
            }
        }
    }
}

@Composable
fun MainScreen(){
    var display by remember { mutableStateOf(("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp,16.dp,16.dp,40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Pantalla
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = display,
                onValueChange = { it ->  display = it },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { display =  "" }) {
                Text("AC")
            }

            Spacer(modifier = Modifier.width(32.dp))


        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { display += "7" }) { Text("7") }
            Button(onClick = { display += "8" }) { Text("8") }
            Button(onClick = { display += "9" }) { Text("9") }
            Button(onClick = { display += "*" }) { Text("*") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { display += "4" }) { Text("4") }
            Button(onClick = { display += "5" }) { Text("5") }
            Button(onClick = { display += "6" }) { Text("6") }
            Button(onClick = { display += "-" }) { Text("-") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { display += "1"}) { Text("1") }
            Button(onClick = { display += "2"}) { Text("2") }
            Button(onClick = { display += "3"}) { Text("3") }
            Button(onClick = { display += "+"}) { Text("+") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { display += "0" }) { Text("0") }
            Button(onClick = { display += "0" }) { Text(".") }
            Button(onClick = { display = evaluarExpresion(display) }) { Text("=")}
            Button(onClick = { display += "/" }) { Text("/") }
        }
    }
}

fun convertirAPostfija(expresion: String): List<String> {
    val resultado = mutableListOf<String>()                         //Adecuacion del metodo de djisktra
    val pila = Stack<Char>()                                        //para convertir una infija a postfija
    val numero = StringBuilder()                                    //ej: 2+2         ==         22+
                                                                    //asegurando que la operacion se realice correctamente
    val precedencia = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2)

    for (c in expresion) {
        when {
            c.isDigit() || c == '.' -> {
                numero.append(c)
            }
            c in precedencia.keys -> {
                if (numero.isNotEmpty()) {
                    resultado.add(numero.toString())
                    numero.clear()
                }
                while (pila.isNotEmpty() && precedencia[pila.peek()]!! >= precedencia[c]!!) {
                    resultado.add(pila.pop().toString())
                }
                pila.push(c)
            }
        }
    }
    if (numero.isNotEmpty()) resultado.add(numero.toString())

    while (pila.isNotEmpty()) resultado.add(pila.pop().toString())
    return resultado
}

fun evaluarPostfija(postfija: List<String>): Double {
    val pila = Stack<Double>()
    for (token in postfija) {                                                   //Metodo que utiliza la expresion postfija convertida con el metodo anterior
        when {                                                                  //y resuelve la operacion
            token.toDoubleOrNull() != null -> pila.push(token.toDouble())

            token in listOf("+", "-", "*", "/") -> {
                val b = pila.pop()
                val a = pila.pop()
                val resultado = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> a / b
                    else -> 0.0
                }
                pila.push(resultado)
            }
        }
    }
    return pila.pop()
}

fun evaluarExpresion(expresion: String): String {
    return try {                                                        //Metodo creado unicamente para que en el boton no haya tanto codigo
        val postfija = convertirAPostfija(expresion)
        val resultado = evaluarPostfija(postfija)
        resultado.toString()
    } catch (e: Exception) {
        "Error"
    }
}