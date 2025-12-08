package com.example.practicas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Lista de 20 colores predefinidos (formato ARGB Long para que sea compatible con tu DB)
object NoteColors {
    val colors = listOf(
        0xFFFFFFFF, // Blanco (Default)
        0xFFF28B82, // Rojo Pastel
        0xFFFBBC04, // Naranja
        0xFFFFF475, // Amarillo
        0xFFCCFF90, // Verde Lima
        0xFFA7FFEB, // Verde Azulado
        0xFFCBF0F8, // Cian
        0xFFAECBFA, // Azul Claro
        0xFFD7AEFB, // Morado
        0xFFFDCFE8, // Rosa
        0xFFE6C9A8, // Marrón Claro
        0xFFE8EAED, // Gris
        0xFFF06292, // Rosa Fuerte
        0xFFBA68C8, // Púrpura
        0xFF9575CD, // Violeta Profundo
        0xFF7986CB, // Índigo
        0xFF4FC3F7, // Azul Cielo
        0xFF4DD0E1, // Cian Profundo
        0xFF4DB6AC, // Verde Azulado Medio
        0xFF81C784, // Verde Medio
        0xFFFFD54F  // Ámbar
    )

    // Función auxiliar para obtener Color de Compose desde el Long
    fun getColor(colorLong: Long): Color {
        return Color(colorLong)
    }
}

@Composable
fun ColorSelector(
    selectedColor: Long,
    onColorClick: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(NoteColors.colors) { colorLong ->
            val color = NoteColors.getColor(colorLong)
            val isSelected = colorLong == selectedColor

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onColorClick(colorLong) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seleccionado",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}