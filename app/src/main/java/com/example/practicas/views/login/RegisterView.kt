package com.example.practicas.views.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.components.Alert
import com.example.practicas.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController, loginVM: LoginViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(text = "Crea tu cuenta", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 16.dp))

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") } // NUEVO

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Username") }, singleLine = true,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // CAMPO TELEFONO NUEVO
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("Teléfono (Ej: +52...)") }, singleLine = true,
            shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                loginVM.createUser(email, password, username, phone) {
                    navController.navigate("Home")
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Registrarse", fontWeight = FontWeight.Bold)
        }

        if (loginVM.showAlert) {
            Alert(title = "Alerta", message = "Error al crear usuario", confirmText = "Aceptar", onConfirmClick = { loginVM.closeAlert() }) { }
        }
    }
}