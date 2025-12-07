package com.example.practicas.Vistas

import Drink
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.practicas.Componentes.ExpandableDrinkGroupCard
import com.example.practicas.Modelos.LicoreriaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicoreriaHome(
    navController: NavController,
    viewModel: LicoreriaViewModel = viewModel()
) {
    val families by viewModel.families.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val randomId by viewModel.randomDrinkId.collectAsState()

    // --- NAVEGACIÓN AUTOMÁTICA RANDOM ---
    LaunchedEffect(randomId) {
        randomId?.let { id ->
            navController.navigate("CocktailDetail/$id")
            viewModel.clearRandomNavigation()
        }
    }

    // --- DRAWER (BARRA LATERAL) ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                drawerContainerColor = Color.White
            ) {
                Spacer(Modifier.height(24.dp))
                Text("MENU PRINCIPAL", modifier = Modifier.padding(24.dp), color = Color.Black, fontWeight = FontWeight.Bold)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio (Hub)", color = Color.Black) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("Hub") { popUpTo("Hub") { inclusive = true } }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Valorant Wiki", color = Color.Black) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("ValorantHome")
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("", color = Color.Black, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        // AHORA ES UN MENÚ, NO UN BACK BUTTON
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {

                var searchText by remember { mutableStateOf("") }
                val isSearching = searchText.isNotBlank()

                // BARRA DE BÚSQUEDA
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it; viewModel.searchCocktail(it) },
                    placeholder = { Text("Buscar trago o ingrediente...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE65100),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xFFE65100)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading && families.isEmpty() && searchResults.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFE65100))
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {

                        if (isSearching) {
                            if (searchResults.isEmpty()) {
                                item { Text("Sin resultados.", color = Color.Gray) }
                            } else {
                                items(searchResults) { drink ->
                                    DrinkSearchItem(drink) { navController.navigate("CocktailDetail/${drink.idDrink}") }
                                }
                            }
                        } else {
                            // --- TARJETA RANDOM ---
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp) // Altura controlada
                                        .clickable { viewModel.findRandomCocktail() },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize().background(
                                            Brush.horizontalGradient(listOf(Color(0xFF2196F3), Color(0xFF64B5F6)))
                                        ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Refresh, null, tint = Color.White)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("¿No sabes qué tomar?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // --- FAMILIAS (MISMA ANCHURA Y ESTILO) ---
                            items(families.keys.toList()) { familyName ->
                                val drinksInFamily = families[familyName] ?: emptyList()
                                if (drinksInFamily.isNotEmpty()) {
                                    ExpandableDrinkGroupCard(
                                        groupName = familyName,
                                        drinks = drinksInFamily,
                                        onDrinkClick = { id -> navController.navigate("CocktailDetail/$id") }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper privado para items de búsqueda
@Composable
private fun DrinkSearchItem(drink: Drink, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            AsyncImage(model = drink.strDrinkThumb, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(12.dp))
            Text(drink.strDrink, color = Color.Black, fontWeight = FontWeight.Medium)
        }
    }
}