package com.example.practicas.Componentes

import Agent
import Drink
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun AgentCard(
    agent: Agent,
    onClick: () -> Unit
) {
    // CORRECCIÓN: Forzamos alpha 1f para evitar transparencias blancas
    // Lógica Anti-Blanco y Anti-Transparencia
    val gradientColors = if (agent.backgroundGradientColors.isNotEmpty()) {
        agent.backgroundGradientColors.map { hex ->
            // 1. Convertimos y forzamos opacidad total (tu fix anterior)
            val color = hex.toColor().copy(alpha = 1f)

            // 2. FILTRO NUEVO: Si el color es Blanco Puro, lo cambiamos al oscuro de Valorant
            if (color == Color.White) Color(0xFF0F1923) else color
        }
    } else {
        listOf(Color(0xFF0F1923), Color(0xFF0F1923))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Quitamos el color del contenedor porque el Box lo va a tapar
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradientColors)) // Gradiente Sólido
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                AsyncImage(
                    model = agent.displayIcon,
                    contentDescription = agent.displayName,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Textos en Blanco para contrastar con fondo oscuro
                Column {
                    Text(
                        text = agent.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    agent.role?.let { role ->
                        Text(
                            text = role.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableDrinkGroupCard(
    groupName: String,
    drinks: List<Drink>,
    onDrinkClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Animación de rotación para la flechita
    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)) // Fondo oscuro elegante
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- HEADER (Siempre visible) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // Click para expandir
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Un icono genérico o la primera imagen de la lista
                    if (drinks.isNotEmpty()) {
                        AsyncImage(
                            model = drinks.first().strDrinkThumb,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Flechita animada
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expandir",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(rotationState)
                )
            }

            // --- CONTENIDO EXPANDIBLE ---
            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                    drinks.forEach { drink ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDrinkClick(drink.idDrink) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Miniatura
                            AsyncImage(
                                model = drink.strDrinkThumb,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))

                            // Nombre variante
                            Text(
                                text = drink.strDrink,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        // Separador sutil
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray.copy(alpha = 0.1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HubCard(
    title: String,
    subtitle: String,
    gradient: List<Color>, // ¡VOLVEMOS A PEDIR EL GRADIENTE!
    @DrawableRes imageRes: Int, // Y TAMBIÉN LA IMAGEN
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        // Usamos un Box para apilar elementos uno sobre otro
        Box(modifier = Modifier.fillMaxSize()) {

            // CAPA 1 (Fondo): La Imagen
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // CAPA 2 (Intermedia): El Gradiente Original
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // TRUCO: Le damos transparencia para que se vea la imagen de abajo
                    // Ajusta el 0.75f entre 0.0 (invisible) y 1.0 (sólido) a tu gusto.
                    .alpha(0.75f)
                    .background(Brush.horizontalGradient(gradient))
            )

            // CAPA 3 (Frente): El Texto
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun QuickAccessItem(
    imageUrl: String?,
    name: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(85.dp) // Aumentamos un poquito el ancho (de 80 a 85) para dar aire
            .clickable { onClick() }
    ) {
        // Imagen circular
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TEXTO MEJORADO
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp, // Un poco más pequeño para que quepa más
                lineHeight = 14.sp // Altura de línea compacta
            ),
            color = Color.Black,
            maxLines = 2, // <--- AHORA PERMITE 2 LÍNEAS
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center, // <--- CENTRADO PARA QUE SE VEA ELEGANTE
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun String.toColor(): Color {
    // Si el string viene nulo o vacio, regresamos un color default (Gris oscuro)
    if (this.isEmpty()) return Color(0xFF0F1923)

    return try {
        // Quitamos el # si es que viene (aunque la API de Valorant no lo trae)
        val hex = this.removePrefix("#")

        when (hex.length) {
            // Caso RRGGBB (6 caracteres) -> Le agregamos FF al inicio para el Alpha
            6 -> {
                val colorLong = "FF$hex".toLong(16)
                Color(colorLong)
            }
            // Caso RRGGBBAA (8 caracteres - Formato Valorant API)
            // La API manda R-G-B-A (ej: 371c5cff)
            // Compose necesita A-R-G-B (ej: ff371c5c)
            8 -> {
                val red = hex.substring(0, 2)
                val green = hex.substring(2, 4)
                val blue = hex.substring(4, 6)
                val alpha = hex.substring(6, 8)

                // Reconstruimos el string en orden AARRGGBB
                val argbHex = "$alpha$red$green$blue"

                // Convertimos a Long usando base 16
                Color(argbHex.toLong(16))
            }
            else -> Color(0xFF0F1923) // Formato no reconocido
        }
    } catch (e: Exception) {
        // Si algo falla al convertir, regresamos color default para que no crashee
        Color(0xFF0F1923)
    }
}

fun Drink.getIngredients(): List<Pair<String, String>> {
    val list = mutableListOf<Pair<String, String>>()

    // La API tiene hasta 15 ingredientes. Revisamos uno por uno.
    // Usamos reflection o simple ifs "a la bruta" pero seguro.
    // Para simplificar y no usar reflection (que es lento), lo haremos directo:

    fun addIfValid(ingredient: String?, measure: String?) {
        if (!ingredient.isNullOrBlank()) {
            list.add(ingredient to (measure ?: ""))
        }
    }

    addIfValid(strIngredient1, strMeasure1)
    addIfValid(strIngredient2, strMeasure2)
    addIfValid(strIngredient3, strMeasure3)
    addIfValid(strIngredient4, strMeasure4)
    addIfValid(strIngredient5, strMeasure5)
    addIfValid(strIngredient6, strMeasure6)
    addIfValid(strIngredient7, strMeasure7)
    addIfValid(strIngredient8, strMeasure8)
    addIfValid(strIngredient9, strMeasure9)
    addIfValid(strIngredient10, strMeasure10)
    addIfValid(strIngredient11, strMeasure11)
    addIfValid(strIngredient12, strMeasure12)
    addIfValid(strIngredient13, strMeasure13)
    addIfValid(strIngredient14, strMeasure14)
    addIfValid(strIngredient15, strMeasure15)
    return list
}