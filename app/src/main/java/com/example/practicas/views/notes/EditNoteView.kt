package com.example.practicas.views.notes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.practicas.components.ColorSelector
import com.example.practicas.viewModels.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteView(navController: NavController, notesVM: NotesViewModel, idDoc: String){
    LaunchedEffect(Unit){ notesVM.getNoteById(idDoc) }
    val state = notesVM.state

    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        newImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // BOTÓN CAMBIAR/AGREGAR IMAGEN
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "Cambiar Foto")
                    }
                    IconButton(onClick = { notesVM.deleteNote(idDoc){ navController.popBackStack() } }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                    IconButton(onClick = {
                        // Pasamos newImageUri (si es null, el VM sabrá qué hacer)
                        notesVM.updateNote(idDoc, newImageUri){ navController.popBackStack() }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { pad ->
        Column(modifier = Modifier.padding(pad).fillMaxSize()) {

            ColorSelector(selectedColor = state.color) { notesVM.onColorChange(it) }
            Divider()

            // LOGICA VISUAL IMAGEN:
            // 1. Si seleccionó una nueva de la galería -> Mostrar esa (newImageUri)
            // 2. Si no seleccionó nueva, pero la nota tiene URL -> Mostrar URL (state.imageUrl)

            if (newImageUri != null) {
                // Previsualización local
                Image(
                    painter = rememberAsyncImagePainter(newImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else if (state.imageUrl.isNotEmpty()) {
                // Imagen de Firebase
                Image(
                    painter = rememberAsyncImagePainter(state.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            TextField(
                value = state.title,
                onValueChange = { notesVM.onValue(it,"title") },
                placeholder = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(state.color), unfocusedContainerColor = Color(state.color),
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = state.note,
                onValueChange = { notesVM.onValue(it, "note") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(state.color), unfocusedContainerColor = Color(state.color),
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}