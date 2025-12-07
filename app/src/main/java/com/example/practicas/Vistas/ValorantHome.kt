package com.example.practicas.Vistas

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicas.Componentes.AgentCard
import com.example.practicas.Modelos.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValorantHome(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val agents by viewModel.agents.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.2f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                drawerContainerColor = Color.White // Drawer Blanco
            ) {
                Spacer(Modifier.height(24.dp))
                Text("MENU PRINCIPAL", modifier = Modifier.padding(24.dp), color = Color.Black, fontWeight = FontWeight.Bold)
                HorizontalDivider()

                // --- DRAWER ARREGLADO ---
                NavigationDrawerItem(
                    label = { Text("Inicio (Hub)", color = Color.Black) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navega y limpia el stack para no volver atrás infinito
                        navController.navigate("Hub") { popUpTo("Hub") { inclusive = true } }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Licorería", color = Color.Black) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("LicoreriaHome")
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Valorant Agents", color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White // FONDO BLANCO
        ) { padding ->
            LazyColumn(contentPadding = PaddingValues(top = padding.calculateTopPadding(), bottom = 16.dp)) {
                items(agents) { agent ->
                    // Card con Toggle de Favorito
                    AgentCard(agent = agent, onClick = { navController.navigate("Detail/${agent.uuid}") })
                }
            }
        }
    }
}