package com.example.practicas.views.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.components.Alert
import com.example.practicas.viewModels.LoginViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordView(navController: NavController, loginVM: LoginViewModel) {
    var inputData by remember { mutableStateOf("") }
    var showSuccessEmailAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Recuperar Contraseña", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            Text(
                text = "Ingresa tu correo electrónico O tu número de celular registrado.",
                style = MaterialTheme.typography.bodyMedium, color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Input Generico
            OutlinedTextField(
                value = inputData,
                onValueChange = { inputData = it },
                label = { Text("Email o Teléfono (+52...)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (inputData.isNotEmpty()) {
                        loginVM.onForgotPassword(
                            input = inputData,
                            onEmailSent = { showSuccessEmailAlert = true },
                            onPhoneFlow = { navController.navigate("VerifyCodeView") }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Continuar")
            }

            if (loginVM.showAlert) {
                Alert(title = "Error", message = "No se encontró el usuario o el formato es inválido.", confirmText = "Aceptar", onConfirmClick = { loginVM.closeAlert() }) { }
            }

            if (showSuccessEmailAlert) {
                Alert(title = "Correo Enviado", message = "Revisa tu bandeja de entrada para restablecer tu contraseña.", confirmText = "Ir al Login", onConfirmClick = {
                    showSuccessEmailAlert = false
                    navController.popBackStack()
                }) { }
            }
        }
    }
}

// VISTA 2: VERIFICAR CODIGO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeView(navController: NavController, loginVM: LoginViewModel) {
    var code by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Verificación",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ingresa el código de 6 dígitos que te dictamos en la llamada.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = code,
                onValueChange = {
                    if(it.length <= 6) code = it
                },
                label = { Text("Código") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    loginVM.verifyCode(code) {
                        // AQUÍ ES DONDE CAMBIARÍAS LA CONTRASEÑA
                        // Por ahora, redirigimos al Home o a una vista de cambio de pass
                        navController.navigate("ChangePasswordView")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Verificar")
            }

            if (loginVM.showAlert) {
                Alert(
                    title = "Incorrecto",
                    message = "El código ingresado no es válido.",
                    confirmText = "Intentar de nuevo",
                    onConfirmClick = { loginVM.closeAlert() }
                ) { }
            }
        }
    }
}