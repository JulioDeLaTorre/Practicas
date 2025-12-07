package com.example.practicas.Vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practicas.Componentes.HubCard
import com.example.practicas.Componentes.QuickAccessItem
import com.example.practicas.Modelos.FavoritesManager
import com.example.practicas.Modelos.HubViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHub(
    navController: NavController,
    viewModel: HubViewModel = viewModel()
) {
    // 1. OBTENER DATOS
    // IDs guardados en disco (SharedPreferences)
    val favAgentsIds = FavoritesManager.favoriteAgents
    val favCocktailsIds = FavoritesManager.favoriteCocktails // IDs simples

    // Data completa cargada por el ViewModel
    val allAgents by viewModel.allAgents.collectAsState()
    val favDrinksDetails by viewModel.favoriteDrinksData.collectAsState() // Objetos coctel completos

    // Data para secciones sugeridas
    val metaAgents by viewModel.metaAgents.collectAsState()
    val popularDrinks by viewModel.popularDrinks.collectAsState()

    // 2. EFECTO: Refrescar favoritos al volver a esta pantalla
    // Esto asegura que si diste like en el detalle, aparezca aquí al volver.
    LaunchedEffect(Unit) {
        viewModel.refreshFavorites()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                drawerContainerColor = Color.White // FONDO BLANCO
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    "MENÚ PRINCIPAL",
                    modifier = Modifier.padding(start = 24.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                Spacer(Modifier.height(16.dp))

                NavigationDrawerItem(
                    label = { Text("Inicio", color = Color.Black) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )
                NavigationDrawerItem(
                    label = { Text("Valorant Wiki", color = Color.Gray) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("ValorantHome")
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )
                NavigationDrawerItem(
                    label = { Text("Licorería", color = Color.Gray) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("LicoreriaHome")
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                // TopBar transparente
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White // FONDO BLANCO GENERAL
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // --- SECCIÓN 1: CARDS PRINCIPALES ---
                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))

                        HubCard(
                            title = "VALORANT",
                            subtitle = "Agentes & Estrategia",
                            gradient = listOf(Color(0xFFFF4655), Color(0xFFBD3944)), // Rojo Valorant Sólido
                            imageRes = com.example.practicas.R.drawable.valorantlogo,
                            onClick = { navController.navigate("ValorantHome") }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        HubCard(
                            title = "BARMAN",
                            subtitle = "Cocteles & Recetas",
                            gradient = listOf(Color(0xFFFF9800), Color(0xFFF57C00)), // Naranja Sólido
                            com.example.practicas.R.drawable.beerlogo,
                            onClick = { navController.navigate("LicoreriaHome") }
                        )
                    }
                }

                // --- SECCIÓN 2: MIS FAVORITOS (Agentes + Cocteles) ---
                if (favAgentsIds.isNotEmpty() || favDrinksDetails.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "MIS FAVORITOS")

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 2.1 Agentes Favoritos
                            // Filtramos de la lista 'allAgents' los que coincidan con los IDs guardados
                            val myFavAgents = allAgents.filter { favAgentsIds.contains(it.uuid) }
                            items(myFavAgents) { agent ->
                                QuickAccessItem(
                                    imageUrl = agent.displayIcon,
                                    name = agent.displayName,
                                    onClick = { navController.navigate("Detail/${agent.uuid}") }
                                )
                            }

                            // 2.2 Cocteles Favoritos
                            // Usamos la lista 'favDrinksDetails' que el ViewModel cargó específicamente
                            items(favDrinksDetails) { drink ->
                                QuickAccessItem(
                                    imageUrl = drink.strDrinkThumb,
                                    name = drink.strDrink,
                                    onClick = { navController.navigate("CocktailDetail/${drink.idDrink}") }
                                )
                            }
                        }
                    }
                }

                // --- SECCIÓN 3: ACCESO RÁPIDO META ---
                if (metaAgents.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "AGENTES META")

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(metaAgents) { agent ->
                                QuickAccessItem(
                                    imageUrl = agent.displayIcon,
                                    name = agent.displayName,
                                    onClick = { navController.navigate("Detail/${agent.uuid}") }
                                )
                            }
                        }
                    }
                }

                // --- SECCIÓN 4: ACCESO RÁPIDO BEBIDAS ---
                if (popularDrinks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "TRAGOS SUGERIDOS")

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(popularDrinks) { drink ->
                                QuickAccessItem(
                                    imageUrl = drink.strDrinkThumb,
                                    name = drink.strDrink,
                                    onClick = { navController.navigate("CocktailDetail/${drink.idDrink}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Componente pequeño para el título de sección
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Gray, // Gris oscuro
        modifier = Modifier.padding(start = 24.dp, bottom = 16.dp),
        letterSpacing = 1.2.sp
    )
}