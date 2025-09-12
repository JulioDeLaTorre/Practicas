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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.practicas.ui.theme.PracticasTheme

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
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { /* AC */ }) {
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
            Button(onClick = { /* = */ }) { Text("=")}
            Button(onClick = { display += "/" }) { Text("/") }
        }
    }
}