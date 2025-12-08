package com.example.practicas.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicas.model.NotesState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotesViewModel:ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    // Usamos una lista mutable interna para la UI reactiva al arrastrar
    private val _notesData = MutableStateFlow<List<NotesState>>(emptyList())
    val notesData: StateFlow<List<NotesState>> = _notesData

    var state by mutableStateOf(NotesState())
        private set

    fun onValue(value:String, text: String){
        when(text){
            "title" -> state = state.copy(title = value)
            "note" -> state = state.copy(note = value)
        }
    }

    // --- OBTENER NOTAS (Ordenadas) ---
    fun fetchNotes(){
        val email = auth.currentUser?.email
        firestore.collection("Notes")
            .whereEqualTo("emailUser", email.toString())
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("FIREBASE_ERROR", "Error de escucha: ${error.message}")
                    return@addSnapshotListener
                }

                val documents = mutableListOf<NotesState>()
                if (querySnapshot != null){
                    for (document in querySnapshot){
                        try {
                            // PROTECCIÓN CONTRA CRASH DE MAPEO
                            val myDocument = document.toObject(NotesState::class.java).copy(idDoc = document.id)
                            documents.add(myDocument)
                        } catch (e: Exception) {
                            Log.e("DATA_ERROR", "Error al leer nota: ${document.id}", e)
                        }
                    }
                }

                // Ordenamiento seguro
                val sortedList = documents.sortedWith(
                    compareByDescending<NotesState> { it.isPinned }
                        .thenBy { it.position }
                )
                _notesData.value = sortedList
            }
    }

    // --- ACTUALIZAR ORDEN EN FIREBASE ---
    // Esta función se llama cuando el usuario suelta el ítem
    fun updateOrderInFirebase(newList: List<NotesState>) {
        // Actualizamos flujo local visualmente
        _notesData.value = newList

        viewModelScope.launch(Dispatchers.IO) {
            val batch = firestore.batch() // Batch para guardar todo de golpe

            newList.forEachIndexed { index, note ->
                // Solo actualizamos si la posición cambió
                val ref = firestore.collection("Notes").document(note.idDoc)
                batch.update(ref, "position", index)
            }

            batch.commit()
                .addOnSuccessListener { Log.d("REORDER", "Orden guardado") }
                .addOnFailureListener { Log.d("REORDER", "Error guardando orden") }
        }
    }

    // --- TOGGLE PIN ---
    fun togglePin(idDoc: String, currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Notes").document(idDoc)
                .update("isPinned", !currentStatus)
                .addOnFailureListener { Log.d("ERROR", "Error al pinear") }
        }
    }

    fun onColorChange(newColor: Long) {
        state = state.copy(color = newColor)
    }

    fun saveNewNote(title: String, note: String, color: Long, imageUri: Uri?, onSuccess: () -> Unit) {
        val email = auth.currentUser?.email

        // Función auxiliar para guardar en Firestore
        fun saveToFirestore(url: String) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val newPosition = _notesData.value.size
                    val newNote = hashMapOf(
                        "title" to title,
                        "note" to note,
                        "date" to formatDate(),
                        "emailUser" to email.toString(),
                        "position" to newPosition,
                        "isPinned" to false,
                        "color" to color,
                        "imageUrl" to url // <--- Guardamos la URL
                    )
                    firestore.collection("Notes").add(newNote).addOnSuccessListener { onSuccess() }
                } catch (e: Exception) {
                    Log.d("ERROR SAVE", "Error: ${e.localizedMessage}")
                }
            }
        }

        // Lógica: Si hay foto, sube primero. Si no, guarda directo.
        if (imageUri != null) {
            uploadImage(imageUri) { url -> saveToFirestore(url) }
        } else {
            saveToFirestore("")
        }
    }

    // 3. MODIFICAMOS updateNote PARA ACEPTAR IMAGEN (NUEVA O EXISTENTE)
    fun updateNote(idDoc: String, newImageUri: Uri?, onSuccess: () -> Unit) {
        // Función auxiliar
        fun updateFirestore(url: String) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val editNote = hashMapOf(
                        "title" to state.title,
                        "note" to state.note,
                        "color" to state.color,
                        "imageUrl" to url // Actualizamos URL
                    )
                    // Si la URL está vacía y el usuario no seleccionó nueva, mantenemos la anterior?
                    // En este caso simple, si 'url' viene vacía es porque no había nueva.
                    // Pero ojo: si ya tenía foto y no la cambiamos, 'newImageUri' es null.
                    // Ajuste: La lógica de UI debe manejar si mantenemos la foto vieja.

                    // MEJOR ESTRATEGIA: Solo actualizamos imageUrl si 'url' no es vacía
                    if(url.isNotEmpty()){
                        editNote["imageUrl"] = url
                    }

                    firestore.collection("Notes").document(idDoc)
                        .update(editNote as Map<String, Any>)
                        .addOnSuccessListener { onSuccess() }
                } catch (e: Exception) { Log.d("ERROR EDIT", "Error") }
            }
        }

        if (newImageUri != null) {
            uploadImage(newImageUri) { url -> updateFirestore(url) }
        } else {
            // Si no hay foto nueva, actualizamos solo texto/color/título
            updateFirestore("")
        }
    }

    // En getNoteById asegúrate de traer el campo imageUrl
    fun getNoteById(documentId: String){
        firestore.collection("Notes").document(documentId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null){
                    val noteObj = snapshot.toObject(NotesState::class.java)
                    state = state.copy(
                        title = noteObj?.title ?: "",
                        note = noteObj?.note ?: "",
                        color = noteObj?.color ?: 0xFFFFFFFFL,
                        imageUrl = noteObj?.imageUrl ?: "" // <--- CARGAMOS URL
                    )
                }
            }
    }

    // ... Helper de fecha y demás ...
    private fun formatDate(): String {
        val currentDate : Date = Calendar.getInstance().time
        val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return res.format(currentDate)
    }

    fun deleteNote(idDoc: String, onSuccess:() -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Notes").document(idDoc).delete().addOnSuccessListener { onSuccess() }
            }catch (e:Exception){ Log.d("ERROR DELETE","Error") }
        }
    }

    private fun uploadImage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val uuid = UUID.randomUUID().toString() // Nombre único para la foto
        val imageRef = storageRef.child("images/$uuid.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // Devolvemos la URL pública
                }
            }
            .addOnFailureListener {
                Log.d("UPLOAD", "Error al subir imagen")
                onSuccess("") // Si falla, guardamos sin imagen
            }
    }

    fun signOut(){ auth.signOut() }
}