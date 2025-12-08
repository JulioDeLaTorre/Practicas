package com.example.practicas.views.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.components.Alert
import com.example.practicas.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordView(navController: NavController, loginVM: LoginViewModel) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Contraseña", fontWeight = FontWeight.Bold) },
                // 1. ELIMINAMOS LA FLECHA DE REGRESO (navigationIcon)
                // Esto obliga al usuario a terminar el proceso o cerrar la app,
                // evitando estados inconsistentes al volver atrás.
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Por seguridad, ingresa una contraseña nueva distinta a la anterior.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nueva Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (password == confirmPassword && password.isNotEmpty()) {
                        loginVM.updatePassword(password) {
                            Toast.makeText(context, "Contraseña Actualizada", Toast.LENGTH_SHORT).show()

                            // 2. NAVEGACIÓN DIRECTA AL HOME
                            // Limpiamos el historial para que no pueda volver al Login o VerifyCode
                            navController.navigate("Home") {
                                popUpTo("Login") { inclusive = true }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Confirmar y Entrar")
            }

            if(loginVM.showAlert){
                Alert(
                    title = "Atención",
                    message = "No pudimos actualizar la contraseña automáticamente (sesión expirada). Te hemos enviado un correo de recuperación como respaldo.",
                    confirmText = "Ir al Login",
                    onConfirmClick = {
                        loginVM.closeAlert()
                        // En caso de fallo (fallback a correo), sí debemos mandarlo al Login
                        navController.navigate("Login") {
                            popUpTo("Login") { inclusive = true }
                        }
                    }
                ) { }
            }
        }
    }
}