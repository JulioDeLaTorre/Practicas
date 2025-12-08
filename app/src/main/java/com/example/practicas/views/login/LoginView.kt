package com.example.practicas.views.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun LoginView(navController: NavController, loginVM: LoginViewModel) {

    // Ya no usamos fillMaxSize aquí porque el padre (Card) controla el tamaño
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp) // Un poco de aire lateral interno
    ) {

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        var userOrPhone by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // ---- EMAIL ----
        OutlinedTextField(
            value = userOrPhone,
            onValueChange = { userOrPhone = it },
            label = { Text("Email o Teléfono") }, // <--- Cambia el texto
            placeholder = { Text("ej: correo@xyz.com o 8711234567") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            // Usamos Email o Text. Email suele permitir números, así que funciona bien.
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // ---- PASSWORD ----
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp), // Input Moderno
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                loginVM.login(userOrPhone, password) {
                    navController.navigate("Home")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp) // Botón tipo Píldora (Moderno)
        ) {
            Text("Entrar", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clickable {
                    navController.navigate("ForgotPassword")
                }
                .padding(8.dp) // Aumentar área de toque
        )

        if (loginVM.showAlert) {
            Alert(
                title = "Alerta",
                message = "Usuario y/o Contraseña Incorrectos",
                confirmText = "Aceptar",
                onConfirmClick = { loginVM.closeAlert() }
            ) { }
        }
    }
}