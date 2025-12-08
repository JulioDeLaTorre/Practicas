package com.example.practicas.model

data class NotesState(
    val emailUser: String = "",
    val title: String = "",
    val note: String = "",
    val date: String = "",
    val idDoc: String = "",
    val position: Int = 0,      // Para el orden
    val isPinned: Boolean = false, // Para fijar arriba
    val color: Long = 0xFFFFFFFFL,  // Color ARGB (Blanco por defecto)
    val imageUrl: String = ""
)