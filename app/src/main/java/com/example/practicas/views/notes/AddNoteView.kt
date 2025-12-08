package com.example.practicas.views.notes

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image // Icono de imagen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.practicas.components.ColorSelector
import com.example.practicas.components.NoteColors
import com.example.practicas.viewModels.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteView(navController: NavController, notesVM: NotesViewModel) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(NoteColors.colors[0]) }

    // Estado para la imagen seleccionada
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador de galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Nota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // BOTÓN PARA AGREGAR IMAGEN
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "Agregar Foto")
                    }
                    // BOTÓN GUARDAR
                    IconButton(onClick = {
                        notesVM.saveNewNote(title, note, selectedColor, imageUri) {
                            Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { pad ->
        Column(modifier = Modifier.padding(pad).fillMaxSize()) {

            ColorSelector(selectedColor = selectedColor) { selectedColor = it }
            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            // PREVISUALIZACIÓN DE IMAGEN
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            TextField(
                value = title, onValueChange = { title = it },
                placeholder = { Text("Título", fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(selectedColor), unfocusedContainerColor = Color(selectedColor),
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = note, onValueChange = { note = it },
                placeholder = { Text("Escribe tu nota...") },
                modifier = Modifier.fillMaxWidth().weight(1f), // weight para ocupar el resto
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(selectedColor), unfocusedContainerColor = Color(selectedColor),
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}