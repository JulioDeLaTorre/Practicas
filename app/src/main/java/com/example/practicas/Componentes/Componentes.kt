package com.example.practicas.Componentes

import Agent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

@Composable
fun AgentCard(
    agent: Agent,
    onClick: () -> Unit
) {
    // CORRECCIÓN: Forzamos alpha 1f para evitar transparencias blancas
    val gradientColors = if (agent.backgroundGradientColors.isNotEmpty()) {
        agent.backgroundGradientColors.map { it.toColor().copy(alpha = 1f) }
    } else {
        listOf(Color(0xFF0F1923), Color(0xFF374151))
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