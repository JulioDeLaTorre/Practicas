package com.example.practicas.views.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practicas.components.* import com.example.practicas.model.NotesState
import com.example.practicas.viewModels.NotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, notesVM: NotesViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados de UI
    var isSearchActive by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Vista: false = Lista, true = Grid
    var isGridView by remember { mutableStateOf(false) }

    // Filtros
    var isFilterOpen by remember { mutableStateOf(false) }
    var selectedColorFilter by remember { mutableStateOf<Long?>(null) }

    // Datos
    val firebaseNotes by notesVM.notesData.collectAsState()
    val notesList = remember { mutableStateListOf<NotesState>() }

    // --- FUNCIÓN DE ORDENAMIENTO ---
    fun sortAndRefresh(list: List<NotesState>) {
        val sorted = list.sortedWith(
            compareByDescending<NotesState> { it.isPinned }.thenBy { it.position }
        )
        notesList.clear()
        notesList.addAll(sorted)
    }

    // Sincronización con Firebase
    LaunchedEffect(firebaseNotes, query, selectedColorFilter) {
        // 1. Aplicar filtros
        var filtered = if (query.isEmpty()) firebaseNotes else firebaseNotes.filter {
            it.title.contains(query, true) || it.note.contains(query, true)
        }

        if (selectedColorFilter != null) {
            filtered = filtered.filter { it.color == selectedColorFilter }
        }

        // 2. Ordenar y actualizar lista local
        sortAndRefresh(filtered)
    }

    LaunchedEffect(Unit) { notesVM.fetchNotes() }

    // --- LÓGICA DE PIN INSTANTÁNEO (OPTIMISTA) ---
    fun togglePinOptimistic(item: NotesState) {
        // 1. Mandar cambio a Firebase (Segundo plano)
        notesVM.togglePin(item.idDoc, item.isPinned)

        // 2. Actualizar UI INMEDIATAMENTE (Sin esperar a Firebase)
        val index = notesList.indexOfFirst { it.idDoc == item.idDoc }
        if (index != -1) {
            // Invertimos el valor localmente
            val updatedItem = notesList[index].copy(isPinned = !item.isPinned)
            notesList[index] = updatedItem

            // Reordenamos la lista localmente al instante
            // Copiamos la lista actual para reordenarla
            val currentList = notesList.toList()
            sortAndRefresh(currentList)
        }
    }

    // --- LÓGICA DE ARRASTRE ---
    fun moveItem(fromIndex: Int, toIndex: Int) {
        if (toIndex in notesList.indices && fromIndex in notesList.indices) {
            if (!notesList[toIndex].isPinned && !notesList[fromIndex].isPinned) {
                notesList.apply { add(toIndex, removeAt(fromIndex)) }
            }
        }
    }

    // Gestores Drag & Drop
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        onMove = { from, to -> moveItem(from, to) },
        onDragEnd = { notesVM.updateOrderInFirebase(notesList) }
    )

    val gridState = rememberLazyGridState()
    val gridDragDropState = rememberGridDragDropState(
        gridState = gridState,
        onMove = { from, to -> moveItem(from, to) },
        onDragEnd = { notesVM.updateOrderInFirebase(notesList) }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(260.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menú", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text("Home") }, selected = false,
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") }, selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() }; navController.navigate("ProfileView") }
                )
                Spacer(modifier = Modifier.weight(1f))
                Divider()
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") }, selected = false,
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() }; notesVM.signOut(); navController.popBackStack() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        if (!isSearchActive) {
                            Text("Mis Notas", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        } else {
                            TextField(
                                value = query, onValueChange = { query = it },
                                placeholder = { Text("Buscar...") }, singleLine = true,
                                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                            LaunchedEffect(Unit) { focusRequester.requestFocus() }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        if (isSearchActive) {
                            IconButton(onClick = { isSearchActive = false; query = ""; focusManager.clearFocus() }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar")
                            }
                        } else {
                            IconButton(onClick = { isGridView = !isGridView }) {
                                Icon(
                                    imageVector = if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Default.GridView,
                                    contentDescription = "Cambiar Vista"
                                )
                            }
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(Icons.Default.Search, contentDescription = "Buscar")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            floatingActionButton = {
                if (!isSearchActive) {
                    FloatingActionButton(
                        onClick = { navController.navigate("AddNoteView") },
                        containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }
                }
            }
        ) { pad ->
            Column(modifier = Modifier.padding(pad).fillMaxSize()) {

                // BARRA FILTRO COLOR
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedColorFilter != null) "Filtrado por color" else "Todas las notas",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        if (selectedColorFilter != null) selectedColorFilter = null else isFilterOpen = !isFilterOpen
                    }) {
                        Icon(
                            imageVector = if (selectedColorFilter != null) Icons.Default.Close else Icons.Default.FilterList,
                            contentDescription = "Filtro",
                            tint = if (selectedColorFilter != null) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

                // SELECTOR FILTRO
                if (isFilterOpen || selectedColorFilter != null) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(NoteColors.colors) { colorLong ->
                            val color = NoteColors.getColor(colorLong)
                            val isSelected = colorLong == selectedColorFilter
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorFilter = if (isSelected) null else colorLong }
                            )
                        }
                    }
                }

                // --- CONTENIDO ---
                Box(modifier = Modifier.padding(horizontal = 10.dp).weight(1f)) {
                    val isFiltering = query.isNotEmpty() || selectedColorFilter != null

                    // =========================================================
                    // CASO 1: MODO FILTRADO (Solo lectura, pero respetando Grid/List)
                    // =========================================================
                    if (isFiltering) {
                        if (isGridView) {
                            // GRID FILTRADA
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(notesList) { _, item ->
                                    Box(modifier = Modifier.padding(2.dp).fillMaxWidth()) {
                                        CompactCardNote(
                                            title = item.title,
                                            colorCode = item.color,
                                            isPinned = item.isPinned,
                                            onPinClick = { togglePinOptimistic(item) },
                                            onClick = { navController.navigate("EditNoteView/${item.idDoc}") }
                                        )
                                    }
                                }
                            }
                        } else {
                            // LISTA FILTRADA
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                itemsIndexed(notesList) { _, item ->
                                    Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()) {
                                        CardNoteWithPin(item, notesVM, navController) { togglePinOptimistic(item) }
                                    }
                                }
                            }
                        }
                    }
                    // =========================================================
                    // CASO 2: MODO NORMAL (Con Drag & Drop)
                    // =========================================================
                    else {
                        if (isGridView) {
                            // GRID ARRASTRABLE
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                state = gridState,
                                modifier = Modifier.fillMaxSize().gridDragContainer(gridDragDropState)
                            ) {
                                itemsIndexed(notesList, key = { _, item -> item.idDoc }) { index, item ->
                                    Box(
                                        modifier = Modifier.gridDraggableItem(gridDragDropState, index).padding(2.dp).fillMaxWidth()
                                    ) {
                                        CompactCardNote(
                                            title = item.title,
                                            colorCode = item.color,
                                            isPinned = item.isPinned,
                                            onPinClick = { togglePinOptimistic(item) }, // Usa la función optimista
                                            onClick = { navController.navigate("EditNoteView/${item.idDoc}") }
                                        )
                                    }
                                }
                            }
                        } else {
                            // LISTA ARRASTRABLE
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize().dragContainer(dragDropState)
                            ) {
                                itemsIndexed(notesList, key = { _, item -> item.idDoc }) { index, item ->
                                    Box(
                                        modifier = Modifier.draggableItem(dragDropState, index).fillMaxWidth().padding(bottom = 8.dp)
                                    ) {
                                        CardNoteWithPin(item, notesVM, navController) { togglePinOptimistic(item) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper actualizado para recibir la acción de Pin personalizada
@Composable
fun CardNoteWithPin(
    item: NotesState,
    notesVM: NotesViewModel,
    navController: NavController,
    onPinAction: () -> Unit // Nuevo parámetro para inyectar la lógica optimista
) {
    CardNote(
        title = item.title, note = item.note, date = item.date,
        colorCode = item.color, imageUrl = item.imageUrl, isPinned = item.isPinned,
        onPinClick = { onPinAction() },
        onClick = { navController.navigate("EditNoteView/${item.idDoc}") }
    )
}

@Composable
fun CompactCardNote(
    title: String, colorCode: Long, isPinned: Boolean,
    onPinClick: () -> Unit, onClick: () -> Unit
) {
    val backgroundColor = Color(colorCode)
    Card(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.aspectRatio(1f).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                fontWeight = FontWeight.Bold, lineHeight = 12.sp, maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { onPinClick() },
                modifier = Modifier.align(Alignment.TopEnd).size(16.dp)
            ) {
                Icon(
                    imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = "Pin",
                    tint = if (isPinned) Color(0xFF4285F4) else Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}//probando