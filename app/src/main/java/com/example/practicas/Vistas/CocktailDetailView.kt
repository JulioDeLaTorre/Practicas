package com.example.practicas.Vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.practicas.Componentes.getIngredients
import com.example.practicas.Modelos.FavoritesManager
import com.example.practicas.Modelos.LicoreriaViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailView(
    navController: NavController,
    drinkId: String, // Recibimos el ID
    viewModel: LicoreriaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // 1. CARGAMOS EL DATO ESPECÍFICO
    // Apenas entramos, le decimos al VM: "Traeme la info de este ID"
    LaunchedEffect(drinkId) {
        viewModel.loadDrinkDetail(drinkId)
    }

    // 2. OBSERVAMOS EL ESTADO
    val drink by viewModel.selectedDrink.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                    }
                },
                actions = {
                    // Solo mostramos el botón si ya cargó el trago
                    drink?.let { d ->
                        val isFav = FavoritesManager.isCocktailFavorite(d.idDrink)
                        IconButton(onClick = { FavoritesManager.toggleCocktailFavorite(d.idDrink) }) {
                            Icon(
                                imageVector = if(isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if(isFav) Color.Red else Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            if (drink == null || isLoading) {
                // ESTADO DE CARGA
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFE65100)
                )
            } else {
                // ESTADO DE ÉXITO: Mostramos la info
                // Usamos 'let' para trabajar seguro con la variable no-nula 'it'
                drink?.let { currentDrink ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        // IMAGEN
                        AsyncImage(
                            model = currentDrink.strDrinkThumb,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // TITULO
                        Text(
                            text = currentDrink.strDrink,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // INGREDIENTES
                        Text(
                            text = "Ingredientes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57C00)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        currentDrink.getIngredients().forEach { (name, measure) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, color = Color.Black)
                                Text(measure, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // PREPARACIÓN
                        Text(
                            text = "Preparación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57C00)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentDrink.strInstructionsES ?: currentDrink.strInstructions ?: "Mezclar y servir.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            lineHeight = 24.sp // Mejor lectura
                        )
                    }
                }
            }
        }
    }
}