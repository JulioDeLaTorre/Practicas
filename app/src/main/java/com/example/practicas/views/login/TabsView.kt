package com.example.practicas.views.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.viewModels.LoginViewModel

@Composable
fun TabsView(navController: NavController, loginVM: LoginViewModel) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Iniciar Sesión", "Registrarse")

    // Usamos Surface para asegurar el fondo correcto del tema (Blanco usualmente)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // O Color.White si quieres forzarlo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), // Margen lateral para que no toque los bordes de la pantalla
            verticalArrangement = Arrangement.Center, // ESTO CENTRA TODO VERTICALMENTE
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TABS (Diseño limpio y ancho)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                divider = { } // Sin línea divisoria para máximo minimalismo
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                // Resaltamos el texto seleccionado, atenuamos el no seleccionado
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.Gray,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        modifier = Modifier.padding(vertical = 12.dp) // Un poco de aire en los tabs
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Espacio elegante entre Tabs y Formulario

            // CONTENIDO DEL FORMULARIO
            // Al cambiar el tab, se renderiza la vista correspondiente abajo
            when (selectedTab) {
                0 -> LoginView(navController, loginVM)
                1 -> RegisterView(navController, loginVM)
            }
        }
    }
}