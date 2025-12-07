package com.example.practicas.Vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.practicas.Componentes.toColor
import com.example.practicas.Modelos.FavoritesManager
import com.example.practicas.Modelos.HomeViewModel
@Composable
fun DetailView(
    navController: NavController,
    agentUuid: String,
    onBack: () -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val agents by viewModel.agents.collectAsState()
    val agent = agents.find { it.uuid == agentUuid }

    if (agent == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    // --- PREPARACIÓN DE COLORES ---
    // 1. Convertimos a color.
    // 2. Forzamos alpha = 1f para evitar niebla blanca.
    // 3. Si es blanco puro (FFFFFFFF), lo cambiamos a oscuro para que no queme los ojos.
    val gradientColors = if (agent.backgroundGradientColors.isNotEmpty()) {
        agent.backgroundGradientColors.map { hex ->
            val color = hex.toColor().copy(alpha = 1f)
            if (color == Color.White) Color(0xFF0F1923) else color
        }
    } else {
        listOf(Color(0xFF0F1923), Color(0xFF0F1923))
    }

    val backgroundBrush = Brush.verticalGradient(colors = gradientColors)

    // Verificamos si es favorito actualmente
    // Nota: Como FavoritesManager usa mutableStateListOf, esto recompondrá la UI al cambiar.
    val isFav = FavoritesManager.isAgentFavorite(agent.uuid)

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush) // Fondo Sólido de la API
        ) {

            // --- CONTENIDO SCROLLABLE ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp)
            ) {

                // 1. HEADER (Imágenes)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    // Patrón de fondo
                    AsyncImage(
                        model = agent.background,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.4f),
                        contentScale = ContentScale.Crop
                    )

                    // Retrato del Personaje
                    AsyncImage(
                        model = agent.fullPortrait,
                        contentDescription = agent.displayName,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxHeight(0.95f)
                            .offset(y = 20.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // 2. INFO DEL AGENTE
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .offset(y = (-20).dp)
                ) {
                    Text(
                        text = agent.displayName.uppercase(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        agent.role?.displayIcon?.let { iconUrl ->
                            AsyncImage(
                                model = iconUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                    .padding(4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = agent.role?.displayName ?: "Unknown",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "BIOGRAFÍA",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = agent.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "HABILIDADES",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. LISTA DE HABILIDADES
                    agent.abilities.forEach { ability ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            AsyncImage(
                                model = ability.displayIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = ability.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ability.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // --- BOTONES FLOTANTES SUPERIORES (Atrás y Favorito) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingValues.calculateTopPadding() + 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Atrás
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }

                // Botón Favorito (Toggle)
                IconButton(
                    onClick = { FavoritesManager.toggleAgentFavorite(agent.uuid) },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFav) Color.Red else Color.White // Rojo si es fav
                    )
                }
            }
        }
    }
}