package com.example.practicas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.practicas.ui.theme.PracticasTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background){
                    GreetingPractica()
                }
            }
        }
    }
}

@Composable
fun GreetingPractica() {
    //val context= LocalContext.current
    //var texto by remember { mutableStateOf("") }
    //var text by remember { mutableStateOf("") }
    var ValorA by remember { mutableStateOf("") }
    var ValorB by remember { mutableStateOf("") }
    var Resultado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
/*      Row(){
            Text(text = "Escribe tu nombre:",
                 color = Color.Green,
                 fontFamily = FontFamily.SansSerif)

        }

        Row(){
            OutlinedTextField(
                value=texto,
                label={Text("Nombre")},
                onValueChange ={texto=it}
            )
        }

        Row(){ //Codigo del profe para hacer una funcionalidad basica al boton
            OutlinedButton(onClick = { /*TODO*/
                Toast.makeText(
                    context, //val context= LocalContext.current
                    texto,

                    Toast.LENGTH_LONG
                ).show()
            }) {
                Text(text = "Enviar")
            }
        }

        Row(Modifier.align(Alignment.CenterHorizontally )){
            OutlinedTextField(
                value = text,
                label = {Text("ValorA")},
                onValueChange = {text = it}
            )
        }

 */

        Row(Modifier.align(Alignment.CenterHorizontally )){
            OutlinedTextField(
                value = ValorA,
                label = {Text("ValorA")},
                onValueChange = {ValorA = it}
            )
        }

        Row(Modifier.align(Alignment.CenterHorizontally )){
            OutlinedTextField(
                value = ValorB,
                label = {Text("ValorB")},
                onValueChange = {ValorB = it}
            )
        }

        Row(){
            OutlinedButton(onClick = {
                val A = ValorA.toInt()
                val B = ValorB.toInt()
                val C = A+B
                Resultado = C.toString()
            }) {
                Text(text = "Sumar")
            }

            OutlinedButton(onClick = {
                ValorA = ""
                ValorB = ""
                Resultado = ""
            }) {
                Text(text = "Borrar")
            }
        }

        Row(Modifier.align(Alignment.CenterHorizontally )){
            OutlinedTextField(
                value = Resultado,
                label = {Text("Resultado")},
                onValueChange = {Resultado = it}
            )
        }


    }
}
