package com.example.practicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = display,
                onValueChange = { it -> display = it },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,

            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(modifier = Modifier.weight(3f))
            BotonCalculadora("AC", onClick = { display =  "" })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BotonCalculadora("7",onClick = { display += "7" })
            BotonCalculadora("8",onClick = { display += "8" })
            BotonCalculadora("9",onClick = { display += "9" })
            BotonCalculadora("*",onClick = { display += "*" })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BotonCalculadora("4",onClick = { display += "4" })
            BotonCalculadora("5",onClick = { display += "5" })
            BotonCalculadora("6",onClick = { display += "6" })
            BotonCalculadora("-",onClick = { display += "-" })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BotonCalculadora("1",onClick = { display += "1" })
            BotonCalculadora("2",onClick = { display += "2" })
            BotonCalculadora("3",onClick = { display += "3" })
            BotonCalculadora("+",onClick = { display += "+" })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BotonCalculadora("0",onClick = { display += "0" })
            BotonCalculadora(".",onClick = { display += "." })
            BotonCalculadora("=", onClick = { display = evaluarExpresion(display) })
            BotonCalculadora("/",onClick = { display += "/" })
        }
    }}
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
@Composable
fun BotonCalculadora(
    symbol: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(89.3.dp) // Ancho fijo para cada botón
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Fondo transparente (sin relleno)
            contentColor = Color.Black // Color del texto en negro
        ),
        border = BorderStroke(1.dp, Color.Black), // Borde negro de 1dp
        shape = RectangleShape // Esto asegura una forma cuadrada o ligeramente redondeada, no circular
    ) {
        Text(symbol)
    }
}