package com.example.practicas.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CardNote(
    title: String,
    note: String,
    date: String,
    colorCode: Long,
    imageUrl: String,
    isPinned: Boolean,
    onPinClick: () -> Unit,
    onClick: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    val backgroundColor = Color(colorCode)

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Click en toda la tarjeta para editar
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 1. IMAGEN (Si existe)
            if (imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Imagen de la nota",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Altura fija para uniformidad
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // 2. CABECERA (Título, Fecha, Pin, Editar)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }

                // BOTÓN PIN (Tachuela Azul)
                IconButton(onClick = { onPinClick() }) {
                    Icon(
                        imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = "Pin",
                        tint = if (isPinned) Color(0xFF4285F4) else Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.graphicsLayer { rotationZ = 45f } // Rotación estética
                    )
                }

                // BOTÓN EDITAR (Lápiz)
                IconButton(onClick = { onClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Black.copy(alpha = 0.1f)
            )

            // 3. CONTENIDO DE LA NOTA
            if (note.isNotEmpty()) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // (Opcional: Si mantienes la alerta de preview, va aquí.
            // Si no la usas, puedes borrar este bloque)
            if (showAlert) {
                Alert(
                    title = title,
                    message = note,
                    confirmText = "Aceptar",
                    onConfirmClick = { showAlert = false },
                    onDismissClick = { showAlert = false }
                )
            }
        }
    }
}