package com.example.practicas.views.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practicas.model.UserModel
import com.example.practicas.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(navController: NavController, loginVM: LoginViewModel) {

    // Inicializamos con texto vacío
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Estado de carga para evitar mostrar datos vacíos de golpe
    var isLoading by remember { mutableStateOf(true) }

    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // EFECTO PARA TRAER DATOS
    LaunchedEffect(Unit) {
        loginVM.getCurrentUserData { user ->
            if (user != null) {
                username = user.username
                email = user.email
                // Verificamos null en phone por si la base de datos no tiene el campo aún
                phone = user.phoneNumber
            }
            isLoading = false // Ya terminó de cargar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (!isLoading) { // Solo mostrar acciones si ya cargó
                        if (isEditing) {
                            IconButton(onClick = { isEditing = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.Red)
                            }
                            IconButton(onClick = {
                                loginVM.updateUserProfile(username, phone) {
                                    Toast.makeText(context, "Perfil Actualizado", Toast.LENGTH_SHORT).show()
                                    isEditing = false
                                }
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                        }
                    }
                }
            )
        }
    ) { pad ->
        if (isLoading) {
            // MOSTRAR SPINNER DE CARGA
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // MOSTRAR CONTENIDO
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // AVATAR
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = if (username.isNotEmpty()) username.take(1).uppercase() else "U",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditing) {
                    EditProfileForm(username, phone, { username = it }, { phone = it })
                } else {
                    InfoCard(username, email, phone)
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.navigate("ChangePasswordView") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Cambiar Contraseña")
                }
            }
        }
    }
}

// ... (InfoCard, ProfileItemRow y EditProfileForm se mantienen igual que en la respuesta anterior) ...
// Copia aquí las funciones auxiliares InfoCard, ProfileItemRow y EditProfileForm del mensaje previo si no las tienes.

// --- COMPONENTES AUXILIARES PARA LIMPIEZA DEL CODIGO ---


@Composable
fun InfoCard(username: String, email: String, phone: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileItemRow(icon = Icons.Default.Person, label = "Nombre de usuario", value = username)
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))

            ProfileItemRow(icon = Icons.Default.Email, label = "Email", value = email)
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))

            ProfileItemRow(
                icon = Icons.Default.Phone,
                label = "Teléfono",
                value = if(phone.isNotEmpty()) phone else "No registrado"
            )
        }
    }
}

@Composable
fun ProfileItemRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun EditProfileForm(
    username: String,
    phone: String,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Nombre de usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Teléfono (+52...)") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Text(
            text = "El correo electrónico no se puede modificar.",
            style = MaterialTheme.typography.bodySmall, // O bodySmall
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, start = 4.dp)
        )
    }
}